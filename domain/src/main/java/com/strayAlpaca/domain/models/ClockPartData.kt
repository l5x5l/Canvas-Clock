package com.strayAlpaca.domain.models

import com.strayAlpaca.domain.utils.angleInterval
import java.io.Serializable

data class ClockPartData (
    val clockIdx : Int = 0,
    val clockPartIdx : Int = 0,
    var startAngle : Float = 0f,
    var endAngle : Float = 0f,
    var firstColor : String = "#FFFFFFFF",
    var secondColor : String = "#FFFFFFFF",
    var startRadius : Int = 0,
    var middleRadius : Int = 90,
    var useMiddleRadius : Boolean = true,
    var endRadius : Int = 50,
    var useMiddleLineStroke : Boolean = false,
    var strokeColor : String = "#FFFFFFFF",
    var strokeWidth : Int = 0,
    val uiState : UiStateData = UiStateData(),
    var priority : Int = 0
) : Serializable {
    companion object {
        fun getDefaultClockPartList(clockIdx : Int, partAmount : Int, firstColor : String, secondColor : String, strokeColor: String = "#FF000000", strokeWidth: Int = 0, startRadius: Int = 0, middleRadius: Int = 90, endRadius: Int = 50) : ArrayList<ClockPartData> {
            val range = 360f / partAmount
            val clockPartArray = arrayListOf<ClockPartData>()
            for (i in 0 until partAmount) {
                clockPartArray.add(
                    ClockPartData(
                        clockIdx = clockIdx, clockPartIdx = i,
                        startAngle = (360 + (i * range)) % 360, endAngle = (360 + ((i + 1) * range)) % 360,
                        firstColor = firstColor, secondColor = secondColor,
                        strokeColor = strokeColor, strokeWidth = strokeWidth,
                        startRadius = startRadius, middleRadius = middleRadius, endRadius = endRadius, priority = i)
                )
            }
            return clockPartArray
        }

        fun getClockPartByClock(clockData: ClockData) : ClockPartData {
            return if (clockData.clockPartList.isNotEmpty()){
                val lastClockPart = clockData.clockPartList[clockData.clockPartList.size - 1]
                ClockPartData(
                    clockIdx = clockData.clockIdx, clockPartIdx = lastClockPart.clockPartIdx + 1,
                    startAngle = lastClockPart.endAngle, endAngle = (lastClockPart.endAngle + angleInterval(lastClockPart.startAngle, lastClockPart.endAngle)) % 360,
                    firstColor = lastClockPart.firstColor, secondColor = lastClockPart.secondColor,
                    strokeColor = lastClockPart.strokeColor, strokeWidth = lastClockPart.strokeWidth,
                    startRadius = lastClockPart.startRadius, middleRadius = lastClockPart.middleRadius, endRadius = lastClockPart.endRadius, priority = lastClockPart.priority + 1,
                    useMiddleRadius = lastClockPart.useMiddleRadius, useMiddleLineStroke = lastClockPart.useMiddleLineStroke, uiState = UiStateData(isSelected = false, isNew = true)
                )
            } else {
                ClockPartData(
                    clockIdx = clockData.clockIdx, clockPartIdx = 1,
                    startAngle = 0f, endAngle = 45f, firstColor = "#FF47B5FF", secondColor = "#FFDFF6FF", uiState = UiStateData(isSelected = false, isNew = true)
                )
            }
        }

        fun deepCopy(originalClockPartData : ClockPartData) : ClockPartData{
            return ClockPartData(
                clockIdx = originalClockPartData.clockIdx, clockPartIdx = originalClockPartData.clockPartIdx,
                startAngle = originalClockPartData.startAngle, endAngle = originalClockPartData.endAngle,
                firstColor = originalClockPartData.firstColor, secondColor = originalClockPartData.secondColor,
                strokeColor = originalClockPartData.strokeColor, strokeWidth = originalClockPartData.strokeWidth,
                startRadius = originalClockPartData.startRadius, middleRadius = originalClockPartData.middleRadius, endRadius = originalClockPartData.endRadius,
                useMiddleLineStroke = originalClockPartData.useMiddleLineStroke, useMiddleRadius = originalClockPartData.useMiddleRadius,
                uiState = originalClockPartData.uiState.copy(), priority = originalClockPartData.priority
            )
        }
    }
}