package com.example.canvasclock.ui.page.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.ImageView
import android.widget.RemoteViews
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityClockListBinding
import com.example.canvasclock.ui.page.clock_list.ClockListViewModel
import com.example.canvasclock.ui.recycler.adapter.ClockListPagingAdapter
import com.example.canvasclock.ui.recycler.decoration.Grid2Decoration
import com.example.canvasclock.util.dpToPx
import com.example.canvasclock.util.drawClock
import com.example.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.min

@AndroidEntryPoint
class WidgetActivity : BaseActivity<ActivityClockListBinding>(R.layout.activity_clock_list) {
    private val viewModel : ClockListViewModel by viewModels()
    var widgetId = -1

    private var initWidgetSize = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setRecyclerView()
        setObserver()
        setButton()

        widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        binding.tvTitle.text = getString(R.string.choose_clock)

        initWidgetSize = dpToPx(this, 90)

    }

    private fun setObserver() {
        lifecycleScope.launch {
            viewModel.pagingData.collectLatest { pagingData ->
                (binding.rvClockList.adapter as ClockListPagingAdapter).submitData(pagingData)
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

        val appWidgetManger = AppWidgetManager.getInstance(this)
        val remoteView = RemoteViews(this.packageName, R.layout.widget_clock)

        val radius = initWidgetSize
        val bitmap = Bitmap.createBitmap(radius, radius, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawClock(canvas, clockData.clockPartList, initWidgetSize/2, initWidgetSize/2, initWidgetSize / 2)

        remoteView.setImageViewBitmap(R.id.iv_widget_clock_shape, bitmap)

        appWidgetManger.updateAppWidget(widgetId, remoteView)

        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}