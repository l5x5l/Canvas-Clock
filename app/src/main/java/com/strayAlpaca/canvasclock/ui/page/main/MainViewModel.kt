package com.strayAlpaca.canvasclock.ui.page.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.canvasclock.models.EventState
import com.strayAlpaca.canvasclock.models.Time
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.usecase.UseCaseGetMainClock
import com.strayAlpaca.domain.usecase.UseCaseGetRecentClock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class MainViewModel @Inject constructor(
    private val useCaseGetMainClock : UseCaseGetMainClock,
    private val useCaseGetRecentClock: UseCaseGetRecentClock
): ViewModel() {

    private lateinit var timerJob : Job

    private val _time = MutableStateFlow(Time())
    val time = _time.asStateFlow()

    private val _mainClockState : MutableStateFlow<EventState<ArrayList<ClockData>>> = MutableStateFlow(EventState.loading())
    val mainClockState = _mainClockState.asStateFlow()

    private val _clockListState : MutableStateFlow<EventState<ArrayList<ClockData>>> = MutableStateFlow(EventState.loading())
    val clockListState = _clockListState.asStateFlow()

    fun startTimer(){
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
        timerJob = viewModelScope.launch {
            while (true) {
                val calendar = Calendar.getInstance()
                _time.value = Time(
                    hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), second = calendar.get(Calendar.SECOND),
                    day = calendar.get(Calendar.DAY_OF_MONTH), month = calendar.get(Calendar.MONTH) + 1
                )
                delay(1000L)
            }
        }
    }

    fun stopTimer(){
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
    }

    fun tryGetRandomClock(amount : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCaseGetMainClock.execute(amount)
            _mainClockState.update { EventState.success(result) }
        }
    }

    fun tryGetRecentModifyClock(amount : Int?){
        viewModelScope.launch(Dispatchers.IO) {
            val result = useCaseGetRecentClock.execute(amount = amount)
            _clockListState.update { EventState.success(result) }
        }
    }

}