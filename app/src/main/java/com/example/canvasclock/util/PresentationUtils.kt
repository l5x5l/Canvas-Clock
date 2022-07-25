package com.example.canvasclock.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.example.domain.mapper.DomainLayerMapper
import com.example.domain.models.ClockPartData

fun drawClock(canvas : Canvas, clockPartList : ArrayList<ClockPartData>, mx : Int, my : Int, radius : Int) {
    for (clockPart in clockPartList) {
        val coordinateClockPart = DomainLayerMapper.toCoordinateClockPartData(clockPartData = clockPart, viewMx = mx, viewMy = my, viewRadius = radius)

        val paint1 = Paint()
        paint1.color = Color.parseColor(coordinateClockPart.firstColor)
        paint1.style = Paint.Style.FILL

        val path1 = Path()
        path1.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
        path1.lineTo(coordinateClockPart.endLeftCoordinate.x, coordinateClockPart.endLeftCoordinate.y)
        path1.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
        path1.close()
        canvas.drawPath(path1, paint1)

        val paint2 = Paint()
        paint2.color = Color.parseColor(coordinateClockPart.secondColor)
        paint2.style = Paint.Style.FILL

        val path2 = Path()
        path2.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
        path2.lineTo(coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y)
        path2.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
        path2.close()
        canvas.drawPath(path2, paint2)

        if (coordinateClockPart.strokeWidth != 0) {
            val strokePaint = Paint()
            strokePaint.style = Paint.Style.STROKE
            strokePaint.strokeWidth = coordinateClockPart.strokeWidth.toFloat()
            strokePaint.color = Color.parseColor(coordinateClockPart.strokeColor)

            val strokePath = Path()
            strokePath.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
            strokePath.lineTo(coordinateClockPart.endLeftCoordinate.x, coordinateClockPart.endLeftCoordinate.y)
            strokePath.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
            strokePath.lineTo(coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y)
            strokePath.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
            if (coordinateClockPart.useMiddleLineStroke){
                strokePath.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
            }
            canvas.drawPath(strokePath, strokePaint)
        }
    }
}