package com.strayAlpaca.domain.models

import java.io.Serializable

data class ClockData (
    val clockIdx : Int = -1,
    var clockPartList : ArrayList<ClockPartData> = arrayListOf(),
    var hourHandColor : String = "#FF000000",
    var hourHandWidth : Int = 12,
    var minuteHandColor : String = "#FF000000",
    var minuteHandWidth : Int = 6,
    var secondHandColor : String = "#FF000000",
    var secondHandWidth : Int = 6,
    val uiStateData: UiStateData = UiStateData(),
    var lastModifiedTime : String = "20220101000000"
) : Serializable {
    companion object {
        fun getDefaultClockList() : ArrayList<ClockData> {
            val defaultClockList = arrayListOf<ClockData>()

            defaultClockList.add(
                ClockData(
                    clockIdx = 0,
                    clockPartList = arrayListOf(
                        ClockPartData(clockIdx = 0, clockPartIdx = 0, startAngle = 45f, endAngle = 180f, firstColor = "#FF47B5FF", secondColor = "#FF47B5FF", startRadius = 0, middleRadius = 50, endRadius = 100, useMiddleRadius = false, strokeColor = "#FF000000", strokeWidth = 0, priority = 0),
                        ClockPartData(clockIdx = 0, clockPartIdx = 1, startAngle = 180f, endAngle = 270f, firstColor = "#FFDFF6FF", secondColor = "#FFDFF6FF", startRadius = 0, middleRadius = 70, endRadius = 30, useMiddleRadius = true, strokeColor = "#FF000000", strokeWidth = 0, priority = 1),
                        ClockPartData(clockIdx = 0, clockPartIdx = 2, startAngle = 270f, endAngle = 45f, firstColor = "#FF06283D", secondColor = "#FF06283D", startRadius = 0, middleRadius = 50, endRadius = 100, useMiddleRadius = false, strokeColor = "#FF000000", strokeWidth = 0, priority = 2)
                    ),
                    hourHandColor = "#FF000000",
                    hourHandWidth = 16,
                    minuteHandColor = "#FF000000",
                    minuteHandWidth = 12,
                    secondHandColor = "#FFF06060",
                    secondHandWidth = 8,
                    lastModifiedTime = "20220722143000"
                )
            )
            defaultClockList.add(
                ClockData(
                    clockIdx = 1,
                    clockPartList = ClockPartData.getDefaultClockPartList(clockIdx = 1, partAmount = 12, firstColor = "#FFBDF2D5", secondColor = "#FF4B5D67", strokeColor = "#FF000000", strokeWidth = 1, startRadius = 0, middleRadius = 90, endRadius = 50),
                    hourHandColor = "#FF000000",
                    hourHandWidth = 16,
                    minuteHandColor = "#FF000000",
                    minuteHandWidth = 12,
                    secondHandColor = "#FFF06060",
                    secondHandWidth = 8,
                    lastModifiedTime = "20220722143000"
                )
            )
            defaultClockList.add(
                ClockData(
                    clockIdx = 2,
                    clockPartList = ClockPartData.getDefaultClockPartList(clockIdx = 2, partAmount = 8, firstColor = "#FF000000", secondColor = "#FFFFFFFF", strokeColor = "#FF000000", strokeWidth = 2, startRadius = 0, middleRadius = 90, endRadius = 50),
                    hourHandColor = "#FF000000",
                    hourHandWidth = 16,
                    minuteHandColor = "#FF000000",
                    minuteHandWidth = 12,
                    secondHandColor = "#FFF06060",
                    secondHandWidth = 8,
                    lastModifiedTime = "20220722143000"
                )
            )

            return defaultClockList
        }

        fun deepCopy(originalClockData: ClockData) : ClockData{
            val copiedClockPartList = arrayListOf<ClockPartData>()
            for (clockPart in originalClockData.clockPartList) {
                copiedClockPartList.add(ClockPartData.deepCopy(clockPart))
            }

            return ClockData(
                clockIdx = originalClockData.clockIdx, clockPartList = copiedClockPartList,
                hourHandColor = originalClockData.hourHandColor, hourHandWidth = originalClockData.hourHandWidth,
                minuteHandColor = originalClockData.minuteHandColor, minuteHandWidth = originalClockData.minuteHandWidth,
                secondHandColor = originalClockData.secondHandColor, secondHandWidth = originalClockData.secondHandWidth,
                uiStateData = originalClockData.uiStateData.copy(), lastModifiedTime = originalClockData.lastModifiedTime
            )
        }

        fun getColorSet(clock : ClockData) : MutableSet<String> {
            val colorSet = mutableSetOf<String>()
            for (clockPart in clock.clockPartList){
                colorSet.add(clockPart.firstColor.uppercase())
                colorSet.add(clockPart.secondColor.uppercase())
                colorSet.add(clockPart.strokeColor.uppercase())
            }
            return colorSet
        }
    }
}
