package com.strayAlpaca.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.util.drawClock
import com.strayAlpaca.canvasclock.util.drawClockIcon
import com.strayAlpaca.domain.models.ClockPartData
import kotlin.math.min

class ClockShapeView(context: Context, attrs : AttributeSet) : View(context, attrs) {
    private lateinit var clockPartList : ArrayList<ClockPartData>
    private var mx = 0
    private var my = 0
    private var radius = 0
    private var isIconMode = false

    init {
        isIconMode = context.obtainStyledAttributes(attrs, R.styleable.ClockShapeView).getBoolean(R.styleable.ClockShapeView_iconMode, false)
    }

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
            if (isIconMode){
                drawClockIcon(canvas, clockPartList, mx, my, radius)
            } else {
                drawClock(canvas, clockPartList, mx, my, radius)
            }
        }
    }
}