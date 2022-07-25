package com.example.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.canvasclock.util.drawTimeHand
import com.example.domain.models.ClockData
import kotlin.math.min

class ClockTimerView(context : Context, attrs : AttributeSet) : View(context, attrs){
    private var mx = 0
    private var my = 0
    private var radius = 0

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

    fun invalidateTimerHand(){
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        canvas?.let {
            drawTimeHand(canvas = canvas, clock = clockData, mx = mx, my = my, radius = radius, is24HourMode = is24HourMode)
        }
    }
}