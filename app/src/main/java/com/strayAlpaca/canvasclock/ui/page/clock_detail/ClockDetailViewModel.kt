package com.strayAlpaca.canvasclock.ui.page.clock_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.canvasclock.config.MutableEventFlow
import com.strayAlpaca.canvasclock.config.asEventFlow
import com.strayAlpaca.canvasclock.models.EventState
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.canvasclock.ui.widget.ClockWidgetManager
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.usecase.UseCaseDeleteClock
import com.strayAlpaca.domain.usecase.UseCaseGetClockCount
import com.strayAlpaca.domain.usecase.UseCaseWidgetClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockDetailViewModel @Inject constructor(
    private val useCaseGetClockCount: UseCaseGetClockCount,
    private val useCaseDeleteClock: UseCaseDeleteClock,
    private val useCaseWidgetClock: UseCaseWidgetClock
) : ViewModel() {

    private val _mainClockState : MutableStateFlow<EventState<ClockData>> = MutableStateFlow(EventState.loading())
    val mainClockState = _mainClockState.asStateFlow()

    private val _checkClockCountEvent : MutableEventFlow<Int> = MutableEventFlow()
    val checkClockCountEvent = _checkClockCountEvent.asEventFlow()

    private val _deleteClockEvent : MutableEventFlow<Boolean> = MutableEventFlow()
    val deleteClockEvent = _deleteClockEvent.asEventFlow()

    private var isUpdated = false

    fun setClockData(clockData: ClockData) {
        ModifyClock.getInstance().initModifyClock(clockData)
        _mainClockState.update { EventState.success(clockData) }
    }

    fun applyModifyClockData(clockData: ClockData) {
        _mainClockState.update { EventState.success(clockData) }
    }

    fun checkClockCount() {
        viewModelScope.launch {
            val response = useCaseGetClockCount.execute()
            _checkClockCountEvent.emit(response)
        }
    }

    fun deleteClockEvent() {
        mainClockState.value.value?.let {
            viewModelScope.launch {
                val response = useCaseDeleteClock.execute(it.clockIdx)
                _deleteClockEvent.emit(response == 1)

                val changedWidgetIds = useCaseWidgetClock.getWidgetIdsByClockIdx(it.clockIdx)
                useCaseWidgetClock.setRandomClockToRemoveClockWidget(it.clockIdx)
                ClockWidgetManager.getInstance().updateClockWidget(changedWidgetIds)
            }
        }
    }

    fun setIsUpdated(value : Boolean) {
        isUpdated = value
    }

    fun getIsUpdated() = isUpdated
}