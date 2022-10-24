package com.strayAlpaca.canvasclock.ui.page.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseActivity
import com.strayAlpaca.canvasclock.databinding.ActivityClockListBinding
import com.strayAlpaca.canvasclock.ui.page.clock_list.ClockListViewModel
import com.strayAlpaca.canvasclock.ui.recycler.adapter.ClockListPagingAdapter
import com.strayAlpaca.canvasclock.ui.recycler.decoration.Grid2Decoration
import com.strayAlpaca.canvasclock.util.WidgetSizeProvider
import com.strayAlpaca.canvasclock.util.drawClock
import com.strayAlpaca.canvasclock.util.drawTimeHand
import com.strayAlpaca.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WidgetActivity : BaseActivity<ActivityClockListBinding>(R.layout.activity_clock_list) {
    private val viewModel : ClockListViewModel by viewModels()
    private val widgetViewModel : WidgetClockViewModel by viewModels()
    var widgetId = -1
    private var remoteView : RemoteViews ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setObserver()
        setButton()

        widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        binding.tvTitle.text = getString(R.string.choose_clock)
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.pagingData.collectLatest { pagingData ->
                        (binding.rvClockList.adapter as ClockListPagingAdapter).submitData(pagingData)
                    }
                }

                launch {
                    widgetViewModel.saveClockResult.collect { result ->
                        if (result) {
                            val intent = Intent().apply {
                                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                                action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                            }
                            firebaseAnalytics.logEvent("CREATE_WIDGET", null)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    override fun setRecyclerView() {
        binding.rvClockList.layoutManager = GridLayoutManager(this, 2)
        binding.rvClockList.adapter = ClockListPagingAdapter(::itemClickEvent)
        binding.rvClockList.addItemDecoration(Grid2Decoration(this))
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }
    }

    // 시계 목록 recyclerView 의 아이템 클릭 이벤트입니다.
    private fun itemClickEvent(clockData : ClockData){
        remoteView = RemoteViews(this.packageName, R.layout.widget_clock)
        val appWidgetManger = AppWidgetManager.getInstance(this@WidgetActivity)
        val widgetSize = WidgetSizeProvider(this, appWidgetManger).getWidgetsSize(widgetId)

        val radius = kotlin.math.min(widgetSize.first, widgetSize.second) / 2

        val shapeBitmap = Bitmap.createBitmap(widgetSize.first, widgetSize.second, Bitmap.Config.ARGB_8888)
        val shapeCanvas = Canvas(shapeBitmap)
        drawClock(shapeCanvas, clockData.clockPartList, widgetSize.first / 2, widgetSize.second / 2, radius)

        remoteView?.let {
            it.setImageViewBitmap(R.id.iv_widget_clock_shape, shapeBitmap)
            appWidgetManger.partiallyUpdateAppWidget(widgetId, it)
        }

        val calendar = Calendar.getInstance()
        val handleBitmap = Bitmap.createBitmap(widgetSize.first, widgetSize.second, Bitmap.Config.ARGB_8888)
        val handleCanvas = Canvas(handleBitmap)
        drawTimeHand(canvas = handleCanvas, clock = clockData, mx = widgetSize.first / 2, my = widgetSize.second / 2, radius = radius, is24HourMode = false, hour = calendar.get(Calendar.HOUR), minute = calendar.get(Calendar.MINUTE), second = null )

        remoteView?.let {
            it.setImageViewBitmap(R.id.iv_widget_clock_handle, handleBitmap)
            appWidgetManger.partiallyUpdateAppWidget(widgetId, it)
        }

        widgetViewModel.setClock(clockIdx = clockData.clockIdx, widgetId = widgetId)
    }
}