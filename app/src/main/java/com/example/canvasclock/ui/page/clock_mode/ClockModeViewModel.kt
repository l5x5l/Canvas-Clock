package com.example.canvasclock.ui.page.clock_mode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.canvasclock.models.ClockModeEditableComponent
import com.example.canvasclock.models.Time
import com.example.domain.models.ClockData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ClockModeViewModel @Inject constructor() : ViewModel() {
    private val _time = MutableStateFlow(Time())
    val time = _time.asStateFlow()

    private lateinit var timerJob : Job

    var targetComponent = ClockModeEditableComponent.BACKGROUND

    private val _backgroundColor = MutableStateFlow("#FFFFFFFF")
    val backgroundColor = _backgroundColor.asStateFlow()
    var middleSaveBackgroundColor = backgroundColor.value

    private val _textColor = MutableStateFlow("#FF000000")
    val textColor = _textColor.asStateFlow()
    var middleSaveTextColor = textColor.value

    // 색상 선택 뷰에서 설정중인 색상값
    private val _pickedColor = MutableStateFlow("#FFFFFFFF")
    val pickedColor = _pickedColor.asStateFlow()

    private var clockData = ClockData()

    fun setClockData(clock : ClockData) {
        clockData = ClockData.deepCopy(clock)
    }

    fun getClockData() = clockData

    fun startTimer(){
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
        timerJob = viewModelScope.launch {
            while (true) {
                val calendar = Calendar.getInstance()
                _time.value = Time(
                    hour = calendar.get(Calendar.HOUR_OF_DAY), minute = calendar.get(Calendar.MINUTE), second = calendar.get(
                        Calendar.SECOND),
                    day = calendar.get(Calendar.DAY_OF_MONTH), month = calendar.get(Calendar.MONTH) + 1
                )
                delay(1000L)
            }
        }
    }

    // 초기 배경/텍스트 색상을 변경합니다. (다크모드 관련)
    fun setDarkMode() {
        _backgroundColor.value = "#FF1B1D1F"
        _textColor.value = "#FFFFFFFF"
    }

    fun stopTimer(){
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
    }

    fun setColor(colorString : String) {
        when (targetComponent) {
            ClockModeEditableComponent.BACKGROUND -> {
                _backgroundColor.value = colorString
            }
            ClockModeEditableComponent.TEXT -> {
                _textColor.value = colorString
            }
        }
        _pickedColor.value = colorString
    }

    fun setColorPickerInitColor(){
        _pickedColor.value = when (targetComponent) {
            ClockModeEditableComponent.BACKGROUND -> {
                backgroundColor.value
            }
            ClockModeEditableComponent.TEXT -> {
                textColor.value
            }
        }
    }

    fun rollbackColor(){
        when (targetComponent) {
            ClockModeEditableComponent.BACKGROUND -> {
                _backgroundColor.value = middleSaveBackgroundColor
            }
            ClockModeEditableComponent.TEXT -> {
                _textColor.value = middleSaveTextColor
            }
        }
    }
    fun saveToMiddle() {
        when (targetComponent) {
            ClockModeEditableComponent.BACKGROUND -> {
                middleSaveBackgroundColor = backgroundColor.value
            }
            ClockModeEditableComponent.TEXT -> {
                middleSaveTextColor = textColor.value
            }
        }
    }
}