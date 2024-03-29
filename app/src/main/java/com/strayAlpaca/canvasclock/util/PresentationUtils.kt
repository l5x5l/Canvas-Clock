package com.strayAlpaca.canvasclock.util

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import com.strayAlpaca.domain.mapper.DomainLayerMapper
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.models.CoordinateClockPartData
import kotlin.collections.ArrayList
import kotlin.math.cos
import kotlin.math.sin

fun drawClock(canvas : Canvas, clockPartList : ArrayList<ClockPartData>, mx : Int, my : Int, radius : Int) {
    for (clockPart in clockPartList) {
        val coordinateClockPart = DomainLayerMapper.toCoordinateClockPartData(clockPartData = clockPart, viewMx = mx, viewMy = my, viewRadius = radius)

        drawClockPart(canvas, coordinateClockPart)
    }
}

fun drawClockPart(canvas : Canvas, coordinateClockPart : CoordinateClockPartData) {
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

    // middlePaint 부분이 없다면, 왼쪽과 오른쪽 부품 사이에 1픽셀 정도의 빈 라인이 생기게 된다.
    // 해당 라인을 왼쪽 부분의 색상으로 채우기 위해 middlePaint 를 사용한다.
    val middleLinePaint = Paint()
    middleLinePaint.style = Paint.Style.STROKE
    middleLinePaint.color = Color.parseColor(coordinateClockPart.firstColor)
    canvas.drawLine(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y, coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y, middleLinePaint)

    // rightPaint 부분이 없다면, 다른 부품 사이에 1픽셀 정도의 빈 라인이 생길 수 있다.
    // 해당 라인을 오른쪽 부분의 색상으로 채우기 위해 rightPaint 를 사용한다.
    val rightLinePaint = Paint()
    rightLinePaint.style = Paint.Style.STROKE
    rightLinePaint.color = Color.parseColor(coordinateClockPart.secondColor)
    canvas.drawLine(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y, coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y, rightLinePaint)

    if (coordinateClockPart.strokeWidth != 0f) {
        val strokePaint = Paint()
        strokePaint.style = Paint.Style.STROKE
        strokePaint.strokeWidth = coordinateClockPart.strokeWidth
        strokePaint.color = Color.parseColor(coordinateClockPart.strokeColor)

        val strokePath = Path()
        strokePath.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
        strokePath.lineTo(coordinateClockPart.endLeftCoordinate.x, coordinateClockPart.endLeftCoordinate.y)
        strokePath.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
        strokePath.lineTo(coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y)
        strokePath.close()
        if (coordinateClockPart.useMiddleLineStroke){
            strokePath.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
        }
        canvas.drawPath(strokePath, strokePaint)
    }
}

fun drawClockIcon(canvas : Canvas, clockPartList : ArrayList<ClockPartData>, mx : Int, my : Int, radius : Int) {
    for (clockPart in clockPartList) {
        val coordinateClockPart = DomainLayerMapper.toCoordinateClockPartData(clockPartData = clockPart, viewMx = mx, viewMy = my, viewRadius = radius)

        if (clockPart.firstColor != clockPart.secondColor){
            val paint = Paint()
            paint.color = Color.parseColor("#9EA4AA")
            paint.style = Paint.Style.STROKE

            val path1 = Path()
            path1.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
            path1.lineTo(coordinateClockPart.endLeftCoordinate.x, coordinateClockPart.endLeftCoordinate.y)
            path1.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
            path1.lineTo(coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y)
            path1.lineTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
            path1.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
            canvas.drawPath(path1, paint)
        } else {
            val paint1 = Paint()
            paint1.color = Color.parseColor("#9EA4AA")
            paint1.style = Paint.Style.STROKE

            val path1 = Path()
            path1.moveTo(coordinateClockPart.startCoordinate.x, coordinateClockPart.startCoordinate.y)
            path1.lineTo(coordinateClockPart.endLeftCoordinate.x, coordinateClockPart.endLeftCoordinate.y)
            path1.lineTo(coordinateClockPart.middleCoordinate.x, coordinateClockPart.middleCoordinate.y)
            path1.lineTo(coordinateClockPart.endRightCoordinate.x, coordinateClockPart.endRightCoordinate.y)
            path1.close()
            canvas.drawPath(path1, paint1)
        }

    }
}

fun drawTimeHand(canvas : Canvas, clock : ClockData, mx : Int, my : Int, radius : Int, is24HourMode : Boolean = true, hour : Int, minute : Int, second : Int?){
    val toRadian = (Math.PI / 180).toFloat()

    // 시/분/초침의 최대 두께는 본 View 의 반지름 (=너비/2) 의 0.05배
    val maximumWidth = radius * 0.05f

    // 초침 표시는 선택사항
    if (second != null) {
        val secondAngle = (second * 6 - 90) * toRadian
        val secondX = (mx + (radius * 1f) * cos(secondAngle))
        val secondY = (my + (radius * 1f) * sin(secondAngle))

        val secondPaint = Paint()
        secondPaint.style = Paint.Style.STROKE
        secondPaint.color = Color.parseColor(clock.secondHandColor)
        secondPaint.strokeCap = Paint.Cap.ROUND
        secondPaint.strokeJoin = Paint.Join.ROUND
        secondPaint.strokeWidth = clock.secondHandWidth.toFloat() * 0.05f * maximumWidth
        canvas.drawLine(mx.toFloat(), my.toFloat(), secondX, secondY, secondPaint)
    }

    // 분침 계산 및 표시
    val minuteAngle = (minute * 6 - 90) * toRadian
    val minuteX = (mx + (radius * 0.8f) * cos(minuteAngle))
    val minuteY = (my + (radius * 0.8f) * sin(minuteAngle))

    val minutePaint = Paint()
    minutePaint.style = Paint.Style.STROKE
    minutePaint.color = Color.parseColor(clock.minuteHandColor)
    minutePaint.strokeCap = Paint.Cap.ROUND
    minutePaint.strokeJoin = Paint.Join.ROUND
    minutePaint.strokeWidth = clock.minuteHandWidth.toFloat() * 0.05f * maximumWidth
    canvas.drawLine(mx.toFloat(), my.toFloat(), minuteX, minuteY, minutePaint)

    // 시침 계산 및 표시
    val hourAngle = if (is24HourMode){
        ((hour * 15 + minute * 0.25f - 90) * toRadian)
    } else {
        ((hour * 30 + minute * 0.5f - 90) * toRadian)
    }
    val hourX = (mx + (radius * 0.5f) * cos(hourAngle))
    val hourY = (my + (radius * 0.5f) * sin(hourAngle))

    val hourPaint = Paint()
    hourPaint.style = Paint.Style.STROKE
    hourPaint.color = Color.parseColor(clock.hourHandColor)
    hourPaint.strokeCap = Paint.Cap.ROUND
    hourPaint.strokeJoin = Paint.Join.ROUND
    hourPaint.strokeWidth = clock.hourHandWidth.toFloat() * 0.05f * maximumWidth
    canvas.drawLine(mx.toFloat(), my.toFloat(), hourX, hourY, hourPaint)
}