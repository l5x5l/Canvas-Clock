package com.example.canvasclock.ui.page.clock_mode

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.config.INTENT_KEY_CLOCK
import com.example.canvasclock.databinding.ActivityClockModeBinding
import com.example.canvasclock.models.ClockModeEditableComponent
import com.example.canvasclock.ui.page.main.MainActivity
import com.example.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModeActivity : BaseActivity<ActivityClockModeBinding>(R.layout.activity_clock_mode) {

    private val viewModel : ClockModeViewModel by viewModels()
    private var isShowSettingLayout = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        // 시계 모드는 항상 켜져있어야 하므로 자동 화면 꺼짐이 발생하지 않도록 설정합니다.
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) viewModel.setDarkMode()

        val targetClock = intent.getSerializableExtra(INTENT_KEY_CLOCK) as ClockData
        viewModel.setClockData(targetClock)

        binding.viewClockShape.linkClockInfo(viewModel.getClockData().clockPartList)
        binding.viewClockTime.linkClock(viewModel.getClockData())

        viewModel.startTimer()

        // 화면을 터치하면 상세 수정 화면 표시여부가 변경됩니다.
        binding.root.setOnClickListener {
            if (isShowSettingLayout) {
                hideSettingLayout()
                setFullScreenMode()
            } else {
                showSettingLayout()
                setNormalScreenMode()
            }
        }
        binding.layoutSettings.setOnClickListener {
            hideSettingLayout()
            setFullScreenMode()
        }

        // clockPickerView 설정부분
        binding.viewColorPicker.setButtonCallback(cancelCallback = {
            hideColorPicker()
            showSettingLayout()
            viewModel.rollbackColor()
        }, selectCallback = {
            hideColorPicker()
            showSettingLayout()
            viewModel.saveToMiddle()
        })
        binding.viewColorPicker.attachChangeCallbackFunction(viewModel::setColor)

        setFullScreenMode()
        setObserver()
        setButton()
    }

    override fun onStop() {
        viewModel.stopTimer()
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        viewModel.startTimer()
    }

    private fun setFullScreenMode(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 30) {
            val controller = window.insetsController
            controller?.hide(WindowInsets.Type.systemBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            actionBar?.hide()
        }
    }

    private fun setNormalScreenMode() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= 30) {
            val controller = window.insetsController
            controller?.show(WindowInsets.Type.systemBars())
        } else {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            actionBar?.show()
        }
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.backgroundColor.collect { color ->
                        binding.root.setBackgroundColor(Color.parseColor(color))
                        binding.viewBackgroundColor.ivColor.setBackgroundColor(Color.parseColor(color))
                    }
                }

                launch {
                    viewModel.textColor.collect { color ->
                        binding.tvDate.setTextColor(Color.parseColor(color))
                        binding.tvTime.setTextColor(Color.parseColor(color))
                        binding.viewTextColor.ivColor.setBackgroundColor(Color.parseColor(color))
                    }
                }

                launch {
                    viewModel.pickedColor.collect { color ->
                        binding.viewColorPicker.setColor(color)
                    }
                }
            }
        }
    }

    override fun setButton() {
        binding.layoutbtnBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            startActivity(intent)
        }

        binding.layoutbtnChangeBackgroundColor.setOnClickListener {
            viewModel.targetComponent = ClockModeEditableComponent.BACKGROUND
            showColorPicker()
            hideSettingLayout()
        }

        binding.layoutbtnChangeTextColor.setOnClickListener {
            viewModel.targetComponent = ClockModeEditableComponent.TEXT
            showColorPicker()
            hideSettingLayout()
        }
    }

    private fun showSettingLayout(){
        binding.layoutSettings.visibility = View.VISIBLE
        isShowSettingLayout = true
    }

    private fun hideSettingLayout() {
        binding.layoutSettings.visibility = View.INVISIBLE
        isShowSettingLayout = false
    }

    private fun showColorPicker() {
        binding.viewColorPicker.setColorInUse(ClockData.getColorSet(viewModel.getClockData()))
        binding.viewColorPicker.visibility = View.VISIBLE
        binding.viewColorPicker.setIsInit()
        viewModel.setColorPickerInitColor()
        binding.viewColorPicker.removeIsInit()
    }

    private fun hideColorPicker() {
        binding.viewColorPicker.visibility = View.GONE
    }
}