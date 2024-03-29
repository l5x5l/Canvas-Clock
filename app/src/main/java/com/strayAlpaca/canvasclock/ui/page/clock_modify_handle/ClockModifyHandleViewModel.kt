package com.strayAlpaca.canvasclock.ui.page.clock_modify_handle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strayAlpaca.canvasclock.config.MutableEventFlow
import com.strayAlpaca.canvasclock.config.asEventFlow
import com.strayAlpaca.canvasclock.models.ClockHandAttr
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.canvasclock.ui.widget.ClockWidgetManager
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.usecase.UseCaseInsertClock
import com.strayAlpaca.domain.usecase.UseCaseUpdateClock
import com.strayAlpaca.domain.usecase.UseCaseWidgetClock
import com.strayAlpaca.domain.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockModifyHandleViewModel @Inject constructor(
    private val useCaseUpdateClock: UseCaseUpdateClock,
    private val useCaseInsertClock: UseCaseInsertClock,
    private val useCaseWidgetClock: UseCaseWidgetClock
) : ViewModel() {
    private val _clockData = MutableStateFlow(ModifyClock.getInstance().getOriginalClock())
    val clockData = _clockData.asStateFlow()

    private val _saveModifiedClockResult = MutableEventFlow<Boolean>()
    val saveModifiedClockResult = _saveModifiedClockResult.asEventFlow()

    private val initClock = ClockData.deepCopy(ModifyClock.getInstance().getMiddleSaveClock())
    private var middleSaveClock = ClockData.deepCopy(initClock)


    // 색상 선택 뷰에서 설정중인 색상값
    private val _pickedColor = MutableStateFlow("#FFFFFFFF")
    val pickedColor = _pickedColor.asStateFlow()
    var pickedHandAttr : ClockHandAttr = ClockHandAttr.HOUR

    fun setHandColor(colorString : String) {
        val stateValue = ClockData.deepCopy(clockData.value)
        when (pickedHandAttr) {
            ClockHandAttr.HOUR -> {
                stateValue.hourHandColor = colorString
            }
            ClockHandAttr.MINUTE -> {
                stateValue.minuteHandColor = colorString
            }
            ClockHandAttr.SECOND -> {
                stateValue.secondHandColor = colorString
            }
        }
        _pickedColor.value = colorString
        _clockData.value = stateValue
    }

    fun rollbackColor() {
        val stateValue = ClockData.deepCopy(clockData.value)
        when (pickedHandAttr) {
            ClockHandAttr.HOUR -> {
                stateValue.hourHandColor = middleSaveClock.hourHandColor
            }
            ClockHandAttr.MINUTE -> {
                stateValue.minuteHandColor = middleSaveClock.minuteHandColor
            }
            ClockHandAttr.SECOND -> {
                stateValue.secondHandColor = middleSaveClock.secondHandColor
            }
        }
        _clockData.value = stateValue
    }

    fun setHandWidth(width : Int, handAttr : ClockHandAttr) {
        val stateValue = ClockData.deepCopy(clockData.value)
        when (handAttr) {
            ClockHandAttr.HOUR -> {
                stateValue.hourHandWidth = width
            }
            ClockHandAttr.MINUTE -> {
                stateValue.minuteHandWidth = width
            }
            ClockHandAttr.SECOND -> {
                stateValue.secondHandWidth = width
            }
        }
        _clockData.value = stateValue
    }

    fun setColorPickerInitColor() {
        val colorString = when(pickedHandAttr){
            ClockHandAttr.HOUR -> {
                clockData.value.hourHandColor
            }
            ClockHandAttr.MINUTE -> {
                clockData.value.minuteHandColor
            }
            ClockHandAttr.SECOND -> {
                clockData.value.secondHandColor
            }
        }
        _pickedColor.value = colorString
    }

    /**
     * 색상 변경이나 구간 변경에서 ok 를 클릭하여
     * 중간 세이브 데이터를 변경해야 할 때 호출합니다.
     */
    fun saveToMiddle(){
        middleSaveClock = ClockData.deepCopy(clockData.value)
    }

    fun getCurrentClock() = clockData.value

    fun isChanged() : Boolean {
        return !(initClock.hourHandWidth == clockData.value.hourHandWidth && initClock.hourHandColor == clockData.value.hourHandColor &&
                initClock.minuteHandWidth == clockData.value.minuteHandWidth && initClock.minuteHandColor == clockData.value.minuteHandColor &&
                initClock.secondHandWidth == clockData.value.secondHandWidth && initClock.secondHandColor == clockData.value.secondHandColor)
    }

    fun saveModifiedClockData() {
        viewModelScope.launch {
            clockData.value.lastModifiedTime = getCurrentTime()
            val modifySuccess = useCaseUpdateClock.execute(clockData.value) >= 0
            ModifyClock.getInstance().initModifyClock(clockData.value)

            if (modifySuccess) {
                val changedWidgetIds = useCaseWidgetClock.getWidgetIdsByClockIdx(clockData.value.clockIdx)
                ClockWidgetManager.getInstance().updateClockWidgetTimeHand(appWidgetIds = changedWidgetIds)
            }

            _saveModifiedClockResult.emit(modifySuccess)
        }
    }

    fun tryInsertClock() {
        viewModelScope.launch {
            clockData.value.lastModifiedTime = getCurrentTime()
            val result = useCaseInsertClock.execute(clockData.value)
            _saveModifiedClockResult.emit(result)
        }
    }

    fun getCurrentClockPartAmount() : Int {
        return clockData.value.clockPartList.size
    }
}