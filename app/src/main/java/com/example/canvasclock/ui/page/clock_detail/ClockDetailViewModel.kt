package com.example.canvasclock.ui.page.clock_detail

import androidx.lifecycle.ViewModel
import com.example.canvasclock.models.EventState
import com.example.canvasclock.models.ModifyClock
import com.example.domain.models.ClockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ClockDetailViewModel @Inject constructor() : ViewModel() {

    private val _mainClockState : MutableStateFlow<EventState<ClockData>> = MutableStateFlow(EventState.loading())
    val mainClockState = _mainClockState.asStateFlow()

    fun setClockData(clockData: ClockData) {
        ModifyClock.getInstance().initModifyClock(clockData)
        _mainClockState.update { EventState.success(clockData) }
    }

    fun applyModifyClockData(clockData: ClockData) {
        _mainClockState.update { EventState.success(clockData) }
    }
}