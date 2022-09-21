package com.example.canvasclock.ui.page.widget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvasclock.config.EventFlow
import com.example.canvasclock.config.MutableEventFlow
import com.example.canvasclock.config.asEventFlow
import com.example.domain.usecase.UseCaseWidgetClock
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