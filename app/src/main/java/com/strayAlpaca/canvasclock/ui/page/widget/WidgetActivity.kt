package com.strayAlpaca.canvasclock.ui.page.widget

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseActivity
import com.strayAlpaca.canvasclock.config.SP_KEY_FIRST_ENTER_WIDGET_ACTIVITY
import com.strayAlpaca.canvasclock.databinding.ActivityWidgetBinding
import com.strayAlpaca.canvasclock.ui.custom_components.NoticeDialog
import com.strayAlpaca.canvasclock.ui.page.clock_list.ClockListViewModel
import com.strayAlpaca.canvasclock.ui.recycler.adapter.ClockListPagingAdapter
import com.strayAlpaca.canvasclock.ui.recycler.decoration.Grid2Decoration
import com.strayAlpaca.canvasclock.ui.widget.ClockWidgetProvider
import com.strayAlpaca.canvasclock.util.dpToPx
import com.strayAlpaca.data.datasource.shared_preference.SharedPreference
import com.strayAlpaca.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WidgetActivity : BaseActivity<ActivityWidgetBinding>(R.layout.activity_widget) {
    private val viewModel : ClockListViewModel by viewModels()
    private val widgetViewModel : WidgetClockViewModel by viewModels()
    var widgetId = -1

    private val noticeDialog : NoticeDialog by lazy { NoticeDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setAdViewMargin()
        }

        setRecyclerView()
        setObserver()
        setButton()

        noticeDialog.setDialogData(
            titleResId = R.string.message_battery_setting_for_widget_title,
            bodyResId = R.string.message_battery_setting_for_widget_body,
            imageResId = R.mipmap.img_battery_setting_guide
        )

        widgetId = intent?.extras?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        binding.tvTitle.text = getString(R.string.choose_clock)

        // real ca-app-pub-7971830421694549/3304008198
        // test ca-app-pub-3940256099942544/2247696110
        binding.viewAd.loadAd("ca-app-pub-7971830421694549/3304008198")

        if (checkIsFirstWidgetActivityOpen()) {
            noticeDialog.show(supportFragmentManager, "NoticeDialog")
        }
    }

    override fun onDestroy() {
        binding.viewAd.removeAd()
        super.onDestroy()
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

        binding.ivbtnDialog.setOnClickListener {
            noticeDialog.show(supportFragmentManager, "NoticeDialog")
        }
    }

    private fun setAdViewMargin() {
        binding.tvTitle.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val adViewMarginTop = binding.tvTitle.bottom + dpToPx(this@WidgetActivity, 24)
                val layoutParams = binding.viewAd.layoutParams as FrameLayout.LayoutParams
                layoutParams.topMargin = adViewMarginTop

                binding.tvTitle.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun checkIsFirstWidgetActivityOpen() : Boolean {
        val isFirst = SharedPreference.getInstance().getBoolean(SP_KEY_FIRST_ENTER_WIDGET_ACTIVITY, true)
        if (isFirst) {
            SharedPreference.getInstance().edit().putBoolean(SP_KEY_FIRST_ENTER_WIDGET_ACTIVITY, false).apply()
        }
        return isFirst
    }

    // 시계 목록 recyclerView 의 아이템 클릭 이벤트입니다.
    private fun itemClickEvent(clockData : ClockData){
        widgetViewModel.setClock(clockIdx = clockData.clockIdx, widgetId = widgetId)

        val intent = Intent(baseContext, ClockWidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.setPackage("com.strayAlpaca.canvasclock")
        sendBroadcast(intent)
    }
}