package com.strayAlpaca.canvasclock.ui.custom_components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.databinding.ViewClockInteractionModifyBinding
import com.strayAlpaca.canvasclock.models.ClockPartPointAttr
import com.strayAlpaca.canvasclock.models.ClockPartTimeComponent
import com.strayAlpaca.canvasclock.util.dpToPx
import com.strayAlpaca.canvasclock.util.drawClockPart
import com.strayAlpaca.domain.mapper.DomainLayerMapper
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.models.Coordinate
import kotlin.math.*

class ClockInteractionModifyView(context: Context, attrs: AttributeSet) :
    FrameLayout(context, attrs) {
    private val binding =
        ViewClockInteractionModifyBinding.inflate(LayoutInflater.from(context), this, true)
    private val pointMap = mutableMapOf<ClockPartPointAttr, Coordinate>()

    private val toRadian = Math.PI / 180

    private lateinit var clockPartList: ArrayList<ClockPartData>
    private var mx = 0
    private var my = 0
    private var radius = 0
    private var pointMapSet = false
    private var isMultipleModify = false
    private var isShowGuideline = true

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

    private var guideLinePaint = Paint()
    private var guideCirclePaint = Paint()

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // 수정할 때 사용되는 View 가 ClockInteractionModifyView 를 벗어나는것을 막기 위함입니다.
        radius = min(measuredHeight, measuredWidth) / 2 - dpToPx(context, 20)
    }

    init {
        setWillNotDraw(false)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        mx = (x + measuredWidth / 2).toInt()
        my = (y + measuredHeight / 2).toInt()

        binding.ivbtnShowGuideline.setOnClickListener {
            toggleVisibilityOfInteractionViews()
        }
    }

    fun setMultipleModifyMode() {
        isMultipleModify = true
        binding.viewbtnStartTimePoint.visibility = View.GONE
        binding.viewbtnEndTimePoint.visibility = View.GONE
    }

    // 시간을 조정하는 인터렉션 버튼을 비활성화 합니다.
    fun setTimeIntervalChangeButtonEnable(enabled: Boolean) {
        binding.viewbtnStartTimePoint.isEnabled = enabled
        binding.viewbtnEndTimePoint.isEnabled = enabled
    }

    fun linkClockInfo(newClockPartData: ArrayList<ClockPartData>) {
        clockPartList = newClockPartData
    }

    // 시계부품을 수정하는 View 의 visibility 를 표시합니다.
    private fun toggleVisibilityOfInteractionViews() {
        isShowGuideline = !isShowGuideline
        val visibility = if (isShowGuideline) {
            binding.ivbtnShowGuideline.setImageResource(R.drawable.ic_eye_visible)
            View.VISIBLE
        } else {
            binding.ivbtnShowGuideline.setImageResource(R.drawable.ic_eye_invisible)
            View.GONE
        }

        binding.viewbtnStartPoint.visibility = visibility
        binding.viewbtnMiddlePoint.visibility = visibility
        binding.viewbtnEndPoint.visibility = visibility
        if (!isMultipleModify) {
            binding.viewbtnStartTimePoint.visibility = visibility
            binding.viewbtnEndTimePoint.visibility = visibility
        }
    }


    fun initModifyAction(
        setStartRadius: (Int) -> Unit,
        setMiddleRadius: (Int) -> Unit,
        setEndRadius: (Int) -> Unit,
        setTimeAngle: (Float, ClockPartTimeComponent) -> Unit,
        saveTimeAngle: () -> Unit
    ) {
        binding.viewbtnStartPoint.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewStartPoint = motionEvent.x
                    relativeYPositionInViewStartPoint = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(
                        sqrt(
                            (motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewStartPoint - center.x).pow(2) +
                                    (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewStartPoint - center.y).pow(2)
                        ) / radius, 1f
                    )
                    setStartRadius((weight * 100).toInt())
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }

        binding.viewbtnMiddlePoint.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewMiddlePoint = motionEvent.x
                    relativeYPositionInViewMiddlePoint = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(
                        sqrt(
                            (motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewMiddlePoint - center.x).pow(
                                2
                            ) + (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewMiddlePoint - center.y).pow(
                                2
                            )
                        ) / radius, 1f
                    )
                    setMiddleRadius((weight * 100).toInt())
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }

        binding.viewbtnEndPoint.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewEndPoint = motionEvent.x
                    relativeYPositionInViewEndPoint = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val weight = min(
                        sqrt(
                            (motionEvent.x + view.x + (view.width / 2) - relativeXPositionInViewEndPoint - center.x).pow(
                                2
                            ) + (motionEvent.y + view.y + (view.height / 2) - relativeYPositionInViewEndPoint - center.y).pow(
                                2
                            )
                        ) / radius, 1f
                    )
                    setEndRadius((weight * 100).toInt())
                }

                MotionEvent.ACTION_UP -> {
                    view.performClick()
                }
            }
            true
        }

        binding.viewbtnStartTimePoint.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewStartTimePoint = motionEvent.x
                    relativeYPositionInViewStartTimePoint = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val radian = atan2(
                        (view.y + motionEvent.y + (view.height / 2) - relativeXPositionInViewStartTimePoint - center.y),
                        (view.x + motionEvent.x + (view.width / 2) - relativeYPositionInViewStartTimePoint - center.x)
                    )
                    val degree = (radian * 180 / Math.PI + 90).mod(360.0)

                    setTimeAngle(degree.toFloat(), ClockPartTimeComponent.START)
                }

                MotionEvent.ACTION_UP -> {
                    saveTimeAngle()
                    view.performClick()
                }
            }

            true
        }

        binding.viewbtnEndTimePoint.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    relativeXPositionInViewEndTimePoint = motionEvent.x
                    relativeYPositionInViewEndTimePoint = motionEvent.y
                }

                MotionEvent.ACTION_MOVE -> {
                    val center = pointMap[ClockPartPointAttr.CENTER]!!
                    val radian = atan2(
                        (view.y + motionEvent.y + (view.height / 2) - relativeXPositionInViewEndTimePoint - center.y),
                        (view.x + motionEvent.x + (view.width / 2) - relativeYPositionInViewEndTimePoint - center.x)
                    )
                    val degree = (radian * 180 / Math.PI + 90).mod(360.0)

                    setTimeAngle(degree.toFloat(), ClockPartTimeComponent.END)
                }

                MotionEvent.ACTION_UP -> {
                    saveTimeAngle()
                    view.performClick()
                }
            }

            true
        }
    }

    fun invalidateClock() {
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var tempClockPart: ClockPartData? = null

        if (canvas != null && ::clockPartList.isInitialized) {
            pointMapSet = false
            for (clockPart in clockPartList) {
                val coordinateClockPart = DomainLayerMapper.toCoordinateClockPartData(
                    clockPartData = clockPart,
                    viewMx = mx,
                    viewMy = my,
                    viewRadius = radius
                )

                drawClockPart(canvas, coordinateClockPart)
                if (clockPart.uiState.isSelected && !pointMapSet) {
                    tempClockPart = clockPart
                    val startAngle = (clockPart.startAngle - 90) * toRadian
                    val endAngle = (clockPart.endAngle - 90) * toRadian
                    val startLineX = mx + (radius * cos(startAngle)).toFloat()
                    val startLineY = my + (radius * sin(startAngle)).toFloat()
                    val endLineX = mx + (radius * cos(endAngle)).toFloat()
                    val endLineY = my + (radius * sin(endAngle)).toFloat()

                    pointMapSet = true
                    pointMap[ClockPartPointAttr.START] = coordinateClockPart.startCoordinate
                    pointMap[ClockPartPointAttr.MIDDLE] = coordinateClockPart.middleCoordinate
                    pointMap[ClockPartPointAttr.END] = coordinateClockPart.endLeftCoordinate
                    pointMap[ClockPartPointAttr.START_TIME] = Coordinate(startLineX, startLineY)
                    pointMap[ClockPartPointAttr.END_TIME] = Coordinate(endLineX, endLineY)
                    pointMap[ClockPartPointAttr.CENTER] = Coordinate(mx.toFloat(), my.toFloat())
                }
            }

            if (tempClockPart == null) return

            // draw 순서 때문에 이곳에 배치, 가이드라인 작성부분
            if (isShowGuideline) {
                if (!isMultipleModify) {
                    val linePaint = guideLinePaint
                    linePaint.style = Paint.Style.STROKE
                    linePaint.color = ContextCompat.getColor(
                        context,
                        R.color.guideline_color
                    )//Color.parseColor("#3c000000")
                    linePaint.strokeCap = Paint.Cap.ROUND
                    linePaint.strokeJoin = Paint.Join.ROUND
                    linePaint.strokeWidth = 6f
                    canvas.drawLine(
                        mx.toFloat(),
                        my.toFloat(),
                        pointMap[ClockPartPointAttr.START_TIME]!!.x,
                        pointMap[ClockPartPointAttr.START_TIME]!!.y,
                        linePaint
                    )
                    canvas.drawLine(
                        mx.toFloat(),
                        my.toFloat(),
                        pointMap[ClockPartPointAttr.END_TIME]!!.x,
                        pointMap[ClockPartPointAttr.END_TIME]!!.y,
                        linePaint
                    )
                }

                val circlePaint = guideCirclePaint
                circlePaint.style = Paint.Style.STROKE
                circlePaint.color = ContextCompat.getColor(
                    context,
                    R.color.guideline_color
                )//Color.parseColor("#3c000000")
                circlePaint.strokeWidth = 6f
                canvas.drawCircle(
                    mx.toFloat(),
                    my.toFloat(),
                    tempClockPart.startRadius / 100f * radius,
                    circlePaint
                )
                if (tempClockPart.useMiddleRadius) {
                    canvas.drawCircle(
                        mx.toFloat(),
                        my.toFloat(),
                        tempClockPart.middleRadius / 100f * radius,
                        circlePaint
                    )
                }
                canvas.drawCircle(
                    mx.toFloat(),
                    my.toFloat(),
                    tempClockPart.endRadius / 100f * radius,
                    circlePaint
                )
            }


        }

        changePointViewPosition()
    }

    private fun changePointViewPosition() {
        binding.viewbtnStartPoint.x =
            pointMap[ClockPartPointAttr.START]!!.x - (binding.viewbtnStartPoint.width / 2)
        binding.viewbtnStartPoint.y =
            pointMap[ClockPartPointAttr.START]!!.y - (binding.viewbtnStartPoint.height / 2)

        binding.viewbtnMiddlePoint.x =
            pointMap[ClockPartPointAttr.MIDDLE]!!.x - (binding.viewbtnMiddlePoint.width / 2)
        binding.viewbtnMiddlePoint.y =
            pointMap[ClockPartPointAttr.MIDDLE]!!.y - (binding.viewbtnMiddlePoint.height / 2)

        binding.viewbtnEndPoint.x =
            pointMap[ClockPartPointAttr.END]!!.x - (binding.viewbtnEndPoint.width / 2)
        binding.viewbtnEndPoint.y =
            pointMap[ClockPartPointAttr.END]!!.y - (binding.viewbtnEndPoint.height / 2)

        if (!isMultipleModify) {
            binding.viewbtnStartTimePoint.x =
                pointMap[ClockPartPointAttr.START_TIME]!!.x - (binding.viewbtnStartTimePoint.width / 2)
            binding.viewbtnStartTimePoint.y =
                pointMap[ClockPartPointAttr.START_TIME]!!.y - (binding.viewbtnStartTimePoint.height / 2)

            binding.viewbtnEndTimePoint.x =
                pointMap[ClockPartPointAttr.END_TIME]!!.x - (binding.viewbtnEndTimePoint.width / 2)
            binding.viewbtnEndTimePoint.y =
                pointMap[ClockPartPointAttr.END_TIME]!!.y - (binding.viewbtnEndTimePoint.height / 2)
        }
    }
}