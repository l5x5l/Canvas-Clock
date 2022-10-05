package com.strayAlpaca.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.strayAlpaca.canvasclock.util.drawTimeHand
import com.strayAlpaca.domain.models.ClockData
import kotlin.math.min

class ClockTimerView(context : Context, attrs : AttributeSet) : View(context, attrs){
    private var mx = 0
    private var my = 0
    private var radius = 0

    private var hour = 0
    private var minute = 15
    private var second = 25

    private var is24HourMode = false

    private lateinit var clockData: ClockData

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        radius = min(measuredHeight, measuredWidth) / 2
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mx = (x + measuredWidth / 2).toInt()
        my = (y + measuredHeight / 2).toInt()
    }

    fun linkClock(newClock : ClockData) {
        clockData = newClock
    }

    fun setTime(hour : Int, minute : Int, second : Int){
        this.hour = hour
        this.minute = minute
        this.second = second
    }

    fun change24HourMode(use24HourMode : Boolean) {
        is24HourMode = use24HourMode
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.let {
            if (::clockData.isInitialized) {
                drawTimeHand(
                    canvas = canvas,
                    clock = clockData,
                    mx = mx,
                    my = my,
                    radius = radius,
                    is24HourMode = is24HourMode,
                    hour = hour,
                    minute = minute,
                    second = second
                )
            }
        }
    }
}