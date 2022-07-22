package com.example.domain.models

data class ClockData (
    val clockIdx : Int,
    val clockPartList : ArrayList<ClockPartData>,
    val hourHandColor : String,
    val hourHandWidth : Int,
    val minuteHandColor : String,
    val minuteHandWidth : Int,
    val secondHandColor : String,
    val secondHandWidth : Int,
    val uiStateData: UiStateData = UiStateData(),
    val lastModifiedTime : String
) {
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
                    lastModifiedTime = "2207221430"
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
                    lastModifiedTime = "2207221430"
                )
            )
            defaultClockList.add(
                ClockData(
                    clockIdx = 2,
                    clockPartList = ClockPartData.getDefaultClockPartList(clockIdx = 2, partAmount = 7, firstColor = "#FF000000", secondColor = "#FFFFFFFF", strokeColor = "#FF000000", strokeWidth = 2, startRadius = 0, middleRadius = 90, endRadius = 50),
                    hourHandColor = "#FF000000",
                    hourHandWidth = 16,
                    minuteHandColor = "#FF000000",
                    minuteHandWidth = 12,
                    secondHandColor = "#FFF06060",
                    secondHandWidth = 8,
                    lastModifiedTime = "2207221430"
                )
            )

            return defaultClockList
        }
    }
}
