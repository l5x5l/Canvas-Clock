package com.example.domain.models

import java.io.Serializable

data class ClockPartData (
    val clockIdx : Int,
    val clockPartIdx : Int,
    val startAngle : Float,
    val endAngle : Float,
    val firstColor : String,
    val secondColor : String,
    val startRadius : Int,
    val middleRadius : Int,
    val useMiddleRadius : Boolean = true,
    val endRadius : Int,
    val useMiddleLineStroke : Boolean = false,
    val strokeColor : String,
    val strokeWidth : Int,
    val uiState : UiStateData = UiStateData(),
    var priority : Int
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
    }
}