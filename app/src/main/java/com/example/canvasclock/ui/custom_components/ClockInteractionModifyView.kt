package com.example.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.example.canvasclock.databinding.ViewClockInteractionModifyBinding
import com.example.canvasclock.models.ClockPartPointAttr
import com.example.canvasclock.util.drawClockPart
import com.example.domain.mapper.DomainLayerMapper
import com.example.domain.models.ClockPartData
import com.example.domain.models.Coordinate
import kotlin.math.*

class ClockInteractionModifyView(context : Context, attrs : AttributeSet) : FrameLayout(context, attrs) {
    private val binding = ViewClockInteractionModifyBinding.inflate(LayoutInflater.from(context), this, true)
    private val pointMap = mutableMapOf<ClockPartPointAttr, Coordinate>()

    private val toRadian = Math.PI / 180

    private lateinit var clockPartList : ArrayList<ClockPartData>
    private var mx = 0
    private var my = 0
    private var radius = 0
    private var pointMapSet = false

    // 인터렉션 view mount_down 시 view 내의 position 저장
    private var relativeXPositionInViewStartPoint = 0f
    private var relativeYPositionInViewStartPoint = 0f
    private var relativeXPositionInViewMiddlePoint = 0f
    private var relativeYPositionInViewMiddlePoint = 0f
    private var relativeXPositionInViewEndPoint = 0f
    private var relativeYPositionInViewEndPoint = 0f
    private var relativeXPositionInViewStartTimePoint = 0f
    private var relativeYPositionInViewStartTimePoint = 0f
    private var relativeXPositionInViewEndTimePoint = 0f
    private var relativeYPositionInViewEndTimePoint = 0f

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        radius = min(measuredHeight, measuredWidth) / 2
    }

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mx = (x + measuredWidth / 2).toInt()
        my = (y + measuredHeight / 2).toInt()
    }

    fun timeIntervalChangeDeactivate() {
        // 시작, 종료시각 조정 비활성화
    }

    fun linkClockInfo(newClockPartData : ArrayList<ClockPartData>) {
        clockPartList = newClockPartData
    }

    fun initModifyAction(setStartRadius : (Int) -> Unit, setMiddleRadius : (Int) -> Unit, setEndRadius : (Int) -> Unit) {
        binding.viewbtnStartPoint.setOnTouchListener{ view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewStartPoint = motionEvent.x
                    relativeYPositionInViewStartPoint = motionEvent.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(sqrt((motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewStartPoint - center.x).pow(2) + (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewStartPoint - center.y).pow(2)) / radius, 1f)
                    setStartRadius((weight * 100).toInt())
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }

        binding.viewbtnMiddlePoint.setOnTouchListener{ view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewMiddlePoint = motionEvent.x
                    relativeYPositionInViewMiddlePoint = motionEvent.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(sqrt((motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewMiddlePoint - center.x).pow(2) + (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewMiddlePoint - center.y).pow(2)) / radius, 1f)
                    setMiddleRadius((weight * 100).toInt())
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }

        binding.viewbtnEndPoint.setOnTouchListener{ view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewEndPoint = motionEvent.x
                    relativeYPositionInViewEndPoint = motionEvent.y
                }
                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(sqrt((motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewEndPoint - center.x).pow(2) + (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewEndPoint - center.y).pow(2)) / radius, 1f)
                    setEndRadius((weight * 100).toInt())
                }
                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }
    }

    fun invalidateClock(){
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (canvas != null && ::clockPartList.isInitialized){
            pointMapSet = false
            for (clockPart in clockPartList) {
                val coordinateClockPart = DomainLayerMapper.toCoordinateClockPartData(clockPartData = clockPart, viewMx = mx, viewMy = my, viewRadius = radius)

                drawClockPart(canvas, coordinateClockPart)
                if (clockPart.uiState.isSelected && !pointMapSet) {
                    val startAngle = (clockPart.startAngle - 90) * toRadian
                    val endAngle = (clockPart.endAngle - 90) * toRadian
                    val startLineX = mx + (mx + cos(radius * startAngle)).toFloat()
                    val startLineY = my + (my + sin(radius * startAngle)).toFloat()
                    val endLineX = mx + (mx + cos(radius * endAngle)).toFloat()
                    val endLineY = my + (my + sin(radius * endAngle)).toFloat()

                    pointMapSet = true
                    pointMap[ClockPartPointAttr.START] = coordinateClockPart.startCoordinate
                    pointMap[ClockPartPointAttr.MIDDLE] = coordinateClockPart.middleCoordinate
                    pointMap[ClockPartPointAttr.END] = coordinateClockPart.endLeftCoordinate
                    pointMap[ClockPartPointAttr.START_TIME] = Coordinate(startLineX, startLineY)
                    pointMap[ClockPartPointAttr.END_TIME] = Coordinate(endLineX, endLineY)
                    pointMap[ClockPartPointAttr.CENTER] = Coordinate(mx.toFloat(), my.toFloat())
                }
            }
        }

        changePointViewPosition()
    }

    private fun changePointViewPosition() {
        binding.viewbtnStartPoint.x = pointMap[ClockPartPointAttr.START]!!.x - (binding.viewbtnStartPoint.width / 2)
        binding.viewbtnStartPoint.y = pointMap[ClockPartPointAttr.START]!!.y - (binding.viewbtnStartPoint.height / 2)

        binding.viewbtnMiddlePoint.x = pointMap[ClockPartPointAttr.MIDDLE]!!.x - (binding.viewbtnMiddlePoint.width / 2)
        binding.viewbtnMiddlePoint.y = pointMap[ClockPartPointAttr.MIDDLE]!!.y - (binding.viewbtnMiddlePoint.height / 2)

        binding.viewbtnEndPoint.x = pointMap[ClockPartPointAttr.END]!!.x - (binding.viewbtnEndPoint.width / 2)
        binding.viewbtnEndPoint.y = pointMap[ClockPartPointAttr.END]!!.y - (binding.viewbtnEndPoint.height / 2)
    }
}