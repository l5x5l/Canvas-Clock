package com.example.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.example.canvasclock.util.drawClock
import com.example.domain.models.ClockPartData
import kotlin.math.min

class ClockShapeView(context: Context, attrs : AttributeSet) : View(context, attrs) {
    private lateinit var clockPartList : ArrayList<ClockPartData>
    private var mx = 0
    private var my = 0
    private var radius = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        radius = min(measuredHeight, measuredWidth) / 2
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mx = (x + measuredWidth / 2).toInt()
        my = (y + measuredHeight / 2).toInt()
    }

    fun linkClockInfo(newClockPartData : ArrayList<ClockPartData>) {
        clockPartList = newClockPartData
    }

    fun invalidateClock(){
        invalidate()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas != null && ::clockPartList.isInitialized){
            drawClock(canvas, clockPartList, mx, my, radius)
        }
    }
}