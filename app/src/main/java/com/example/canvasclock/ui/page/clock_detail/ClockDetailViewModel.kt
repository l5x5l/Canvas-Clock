package com.example.canvasclock.ui.page.clock_detail

import androidx.lifecycle.ViewModel
import com.example.canvasclock.models.EventState
import com.example.domain.models.ClockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ClockDetailViewModel @Inject constructor() : ViewModel() {
    private lateinit var clock : ClockData

    private val _mainClockState : MutableStateFlow<EventState<ClockData>> = MutableStateFlow(EventState.loading())
    val mainClockState = _mainClockState.asStateFlow()

    fun setClockData(clockData: ClockData) {
        _mainClockState.update { EventState.success(clockData) }
    }
}