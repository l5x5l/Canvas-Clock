package com.example.domain.mapper

import com.example.domain.models.ClockPartData
import com.example.domain.models.Coordinate
import com.example.domain.models.CoordinateClockPartData
import com.example.domain.models.UiStateData
import kotlin.math.cos
import kotlin.math.sin

object DomainLayerMapper {
    fun toCoordinateClockPartData(clockPartData: ClockPartData, viewRadius : Int, viewMx : Int, viewMy : Int) : CoordinateClockPartData {
        val toRadian = Math.PI / 180
        val maximumStrokeWidth = viewRadius * 0.05f

        val endRadius = (viewRadius * clockPartData.endRadius / 100f)
        val startRadius = (viewRadius * clockPartData.startRadius / 100f)
        val middleRadius = (viewRadius * clockPartData.middleRadius / 100f)

        // 시계 기준으로, 0도가 0시 위치에 오도록 조정
        val startAngle = clockPartData.startAngle - 90
        val endAngle = clockPartData.endAngle - 90

        val startRadian = startAngle * toRadian
        val endRadian = endAngle * toRadian
        val middleRadian = if (startAngle > endAngle) {
            (endAngle + startAngle - 360) * toRadian / 2
        } else {
            (endAngle + startAngle) * toRadian / 2
        }

        val left = Coordinate(x = (viewMx + endRadius * cos(startRadian)).toFloat(), y = (viewMy + endRadius * sin(startRadian)).toFloat())
        val right = Coordinate(x = (viewMx + endRadius * cos(endRadian)).toFloat(), y = (viewMy + endRadius * sin(endRadian)).toFloat())
        val middleX = if (clockPartData.useMiddleRadius) {
            (viewMx + middleRadius * cos(middleRadian)).toFloat()
        } else {
            (left.x + right.x) / 2
        }
        val middleY = if (clockPartData.useMiddleRadius) {
            (viewMy + middleRadius * sin(middleRadian)).toFloat()
        } else {
            (left.y + right.y) / 2
        }
        val middle = Coordinate(x = middleX, y = middleY)
        val start = Coordinate(x = (viewMx + startRadius * cos(middleRadian)).toFloat(), y = (viewMy + startRadius * sin(middleRadian)).toFloat())

        return CoordinateClockPartData(
            startCoordinate = start,
            middleCoordinate = middle,
            endLeftCoordinate = left,
            endRightCoordinate = right,
            firstColor = clockPartData.firstColor,
            secondColor = clockPartData.secondColor,
            strokeColor = clockPartData.strokeColor,
            strokeWidth = (clockPartData.strokeWidth * maximumStrokeWidth * 0.01f),
            useMiddleLineStroke = clockPartData.useMiddleLineStroke,
            uiStateData = UiStateData()
        )
    }
}