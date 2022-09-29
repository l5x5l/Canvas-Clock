package com.strayAlpaca.canvasclock.ui.page.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.canvasclock.config.EventFlow
import com.strayAlpaca.canvasclock.config.MutableEventFlow
import com.strayAlpaca.canvasclock.config.asEventFlow
import com.strayAlpaca.domain.usecase.UseCaseWidgetClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WidgetClockViewModel @Inject constructor(
    private val useCaseWidgetClock: UseCaseWidgetClock
) : ViewModel() {

    private val _saveClockResult = MutableEventFlow<Boolean>()
    val saveClockResult : EventFlow<Boolean> = _saveClockResult.asEventFlow()

    fun setClock(clockIdx : Int, widgetId : Int) {
        viewModelScope.launch {
            useCaseWidgetClock.setWidgetClock(widgetId, clockIdx)
            _saveClockResult.emit(true)
        }
    }
}