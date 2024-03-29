package com.strayAlpaca.canvasclock.ui.page.clock_modify_single_part

import androidx.lifecycle.ViewModel
import com.strayAlpaca.canvasclock.models.ClockPartColorComponent
import com.strayAlpaca.canvasclock.models.ClockPartTimeComponent
import com.strayAlpaca.canvasclock.models.ModifyClock
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.utils.timeToAngle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ClockModifySinglePartViewModel @Inject constructor() : ViewModel() {

    /*
    * init = 수정 화면에 들어올 당시의 시계 데이터
    * middleSaveClock = 색상 선택 뷰에서 색상선택 취소시 돌아갈 데이터
    * currentClock = 현재 수정 화면에서 수정중인 시계 데이터
    * */
    private val initClock = ClockData.deepCopy(ModifyClock.getInstance().getMiddleSaveClock())
    private var middleSaveClock = ClockData.deepCopy(initClock)
    private var currentClock = ClockData.deepCopy(initClock)

    private var selectedClockPartPosition = 0
    private var selectedClockPartAmount = 0

    // 수정중인 부품들의 속성값
    private val _changedClockPartAttr = MutableStateFlow(ClockPartData())
    val changedClockPartAttr = _changedClockPartAttr.asStateFlow()

    // 색상 선택 뷰에서 설정중인 색상값
    private val _pickedColor = MutableStateFlow("#FFFFFFFF")
    val pickedColor = _pickedColor.asStateFlow()
    var pickedColorComponent : ClockPartColorComponent = ClockPartColorComponent.FIRST

    private var pickedTimeComponent : ClockPartTimeComponent = ClockPartTimeComponent.START


    // currentClock 에 시계 부품을 추가합니다.
    fun initViewModel(isAddMode : Boolean){
        if (isAddMode) {
            // 부품을 추가하고자 하는 경우, 기존에 선택되었던 부품들은 선택 해제합니다.
            for (clockPart in currentClock.clockPartList) {
                clockPart.uiState.isSelected = false
            }

            val newClockPart = ClockPartData.getClockPartByClock(currentClock)
            selectedClockPartAmount = 1
            newClockPart.uiState.isSelected = true
            selectedClockPartPosition = currentClock.clockPartList.size
            initClock.clockPartList.add(newClockPart)
            middleSaveClock.clockPartList.add(newClockPart)
            currentClock.clockPartList.add(newClockPart)
            _changedClockPartAttr.value = ClockPartData.deepCopy(newClockPart) // 이걸 그냥 newClockPart 로 사용하면 변경이 안된다.
        } else {
            selectedClockPartAmount = currentClock.clockPartList.filter { it.uiState.isSelected }.size
            for (i in 0 until currentClock.clockPartList.size) {
                if (currentClock.clockPartList[i].uiState.isSelected) {
                    selectedClockPartPosition = i
                    _changedClockPartAttr.value = ClockPartData.deepCopy(currentClock.clockPartList[i])
                    break
                }
            }
        }
    }

    fun getCurrentClockPart() = currentClock.clockPartList

    fun getCurrentClock() = currentClock

    fun selectedClockPartAmount() = selectedClockPartAmount

    // 시간값을 수정합니다.
    fun setTimeAngle(hour : Int, minute : Int, timeComponent: ClockPartTimeComponent) {
        val angle = timeToAngle(is24Mode = false ,hour = hour, minute = minute)
        setTimeAngle(angle, timeComponent)
    }

    // 시간값을 각도 기준으로 수정합니다.
    fun setTimeAngle(degree : Float, timeComponent: ClockPartTimeComponent) {
        pickedTimeComponent = timeComponent
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (pickedTimeComponent == ClockPartTimeComponent.START) {
            currentClock.clockPartList[selectedClockPartPosition].startAngle = degree
            stateValue.startAngle = degree
        } else {
            currentClock.clockPartList[selectedClockPartPosition].endAngle = degree
            stateValue.endAngle = degree
        }
        _changedClockPartAttr.value = stateValue
    }

    // 시계 선택중 취소를 클릭했을 때 시간값을 수정하기 직전의 값으로 설정합니다.
    fun rollbackTime(){
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (pickedTimeComponent == ClockPartTimeComponent.START) {
            val angle = middleSaveClock.clockPartList[selectedClockPartPosition].startAngle
            currentClock.clockPartList[selectedClockPartPosition].startAngle = angle
            stateValue.startAngle = angle
        } else {
            val angle = middleSaveClock.clockPartList[selectedClockPartPosition].endAngle
            currentClock.clockPartList[selectedClockPartPosition].endAngle = angle
            stateValue.endAngle = angle
        }
        _changedClockPartAttr.value = stateValue
    }

    fun setStartRadius(radius : Int) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.startRadius != radius) {
            for (clockPart in currentClock.clockPartList) {
                if (clockPart.uiState.isSelected) {
                    clockPart.startRadius = radius
                }
            }
            stateValue.startRadius = radius
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setMiddleRadius(radius : Int) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.middleRadius != radius) {
            for (clockPart in currentClock.clockPartList) {
                if (clockPart.uiState.isSelected) {
                    clockPart.middleRadius = radius
                }
            }
            stateValue.middleRadius = radius
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setEndRadius(radius : Int) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.endRadius != radius) {
            for (clockPart in currentClock.clockPartList) {
                if (clockPart.uiState.isSelected) {
                    clockPart.endRadius = radius
                }
            }
            stateValue.endRadius = radius
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setStrokeWidth(width : Int) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.strokeWidth != width) {
            for (clockPart in currentClock.clockPartList){
                if (clockPart.uiState.isSelected) {
                    clockPart.strokeWidth = width
                }
            }
            stateValue.strokeWidth = width
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setUseMiddleRadius(isUse : Boolean){
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.useMiddleRadius != isUse){
            for (clockPart in currentClock.clockPartList) {
                if (clockPart.uiState.isSelected) {
                    clockPart.useMiddleRadius = isUse
                }
            }
            stateValue.useMiddleRadius = isUse
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setUseMiddleLineStroke(isUse : Boolean) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        if (stateValue.useMiddleLineStroke != isUse){
            for (clockPart in currentClock.clockPartList) {
                if (clockPart.uiState.isSelected) {
                    clockPart.useMiddleLineStroke = isUse
                }
            }
            stateValue.useMiddleLineStroke = isUse
            _changedClockPartAttr.value = stateValue
        }
    }

    fun setColorPickerInitColor() {
        val colorString = when(pickedColorComponent) {
            ClockPartColorComponent.FIRST -> {
                changedClockPartAttr.value.firstColor
            }
            ClockPartColorComponent.SECOND -> {
                changedClockPartAttr.value.secondColor
            }
            ClockPartColorComponent.STROKE -> {
                changedClockPartAttr.value.strokeColor
            }
        }
        _pickedColor.value = colorString
    }

    fun rollbackColor(){
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        when(pickedColorComponent) {
            ClockPartColorComponent.FIRST -> {
                for (i in 0 until middleSaveClock.clockPartList.size) {
                    currentClock.clockPartList[i].firstColor = middleSaveClock.clockPartList[i].firstColor
                }
                stateValue.firstColor = middleSaveClock.clockPartList[selectedClockPartPosition].firstColor
            }
            ClockPartColorComponent.SECOND -> {
                for (i in 0 until middleSaveClock.clockPartList.size) {
                    currentClock.clockPartList[i].secondColor = middleSaveClock.clockPartList[i].secondColor
                }
                stateValue.secondColor = middleSaveClock.clockPartList[selectedClockPartPosition].secondColor
            }
            ClockPartColorComponent.STROKE -> {
                for (i in 0 until middleSaveClock.clockPartList.size) {
                    currentClock.clockPartList[i].strokeColor = middleSaveClock.clockPartList[i].strokeColor
                }
                stateValue.strokeColor = middleSaveClock.clockPartList[selectedClockPartPosition].strokeColor
            }
        }
        _changedClockPartAttr.value = stateValue
    }

    /**
     * 색상 변경이나 구간 변경에서 ok 를 클릭하여
     * 중간 세이브 데이터를 변경해야 할 때 호출합니다.
     */
    fun saveToMiddle(){
        middleSaveClock = ClockData.deepCopy(currentClock)
    }

    /**
     * color Picker 를 통해
     * 시계에 표시되어야 할 색상 데이터를 변경해야 할 때 호출합니다.
     */
    fun setCurrentColor(colorString : String) {
        val stateValue = ClockPartData.deepCopy(changedClockPartAttr.value)
        when(pickedColorComponent) {
            ClockPartColorComponent.FIRST -> {
                for (clockPart in currentClock.clockPartList){
                    if (clockPart.uiState.isSelected){
                        clockPart.firstColor = colorString
                    }
                }
                stateValue.firstColor = colorString
            }
            ClockPartColorComponent.SECOND -> {
                for (clockPart in currentClock.clockPartList){
                    if (clockPart.uiState.isSelected){
                        clockPart.secondColor = colorString
                    }
                }
                stateValue.secondColor = colorString
            }
            ClockPartColorComponent.STROKE -> {
                for (clockPart in currentClock.clockPartList){
                    if (clockPart.uiState.isSelected){
                        clockPart.strokeColor = colorString
                    }
                }
                stateValue.strokeColor = colorString
            }
        }

        _pickedColor.value = colorString
        _changedClockPartAttr.value = stateValue
    }

    fun saveModifiedContent(){
        for (clockPart in currentClock.clockPartList) {
            if (clockPart.uiState.isSelected)
                clockPart.uiState.isModified = true
        }
        ModifyClock.getInstance().setMiddleSaveClock(currentClock)
    }
}