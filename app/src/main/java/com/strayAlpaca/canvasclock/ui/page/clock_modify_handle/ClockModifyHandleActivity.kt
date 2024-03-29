package com.strayAlpaca.canvasclock.ui.page.clock_modify_handle

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.firebase.analytics.ktx.logEvent
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.BaseActivity
import com.strayAlpaca.canvasclock.config.GlobalApplication
import com.strayAlpaca.canvasclock.config.INTENT_KEY_CLOCK
import com.strayAlpaca.canvasclock.databinding.ActivityClockModifyHandleBinding
import com.strayAlpaca.canvasclock.models.ClockHandAttr
import com.strayAlpaca.canvasclock.ui.custom_components.TwoButtonDialog
import com.strayAlpaca.canvasclock.ui.page.main.MainActivity
import com.strayAlpaca.canvasclock.util.getBottomSheetEnterAnimation
import com.strayAlpaca.canvasclock.util.getBottomSheetExitAnimation
import com.strayAlpaca.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModifyHandleActivity : BaseActivity<ActivityClockModifyHandleBinding>(R.layout.activity_clock_modify_handle) {
    private val viewModel : ClockModifyHandleViewModel by viewModels()
    private val confirmDialog : TwoButtonDialog by lazy { TwoButtonDialog() }

    private var isCreateMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setObserver()
        setSeekbar()

        isCreateMode = intent.getBooleanExtra(INTENT_KEY_CLOCK, false)
        if (isCreateMode) {
            binding.tvbtnSave.text = getString(R.string.create)
        }

        binding.viewClockShape.linkClockInfo(viewModel.clockData.value.clockPartList)

        confirmDialog.setFirstButton(buttonText = R.string.go_back)
        confirmDialog.setSecondButton(buttonText = R.string.cancel, buttonClickEvent = {
            firebaseAnalytics.logEvent("CANCEL_MODIFY_HANDLE", null)
            finish()
        })

        binding.viewColorPicker.setButtonCallback(cancelCallback = {
            hideColorPicker()
            viewModel.rollbackColor()
        }, selectCallback = {
            hideColorPicker()
            viewModel.saveToMiddle()
        })
        binding.viewColorPicker.attachChangeCallbackFunction(viewModel::setHandColor)
    }

    override fun setButton() {
        binding.viewbtnHourHandleColor.root.setOnClickListener {
            viewModel.pickedHandAttr = ClockHandAttr.HOUR
            showColorPicker()
        }

        binding.viewbtnMinuteHandleColor.root.setOnClickListener {
            viewModel.pickedHandAttr = ClockHandAttr.MINUTE
            showColorPicker()
        }

        binding.viewbtnSecondHandleColor.root.setOnClickListener {
            viewModel.pickedHandAttr = ClockHandAttr.SECOND
            showColorPicker()
        }

        // 시계를 수정할 때와 시계를 생성할 때가 다름
        binding.ivbtnBack.setOnClickListener {
            if (isCreateMode) {
                finish()
            } else {
                if (viewModel.isChanged()) {
                    confirmDialog.show(supportFragmentManager, "ConfirmDialog")
                } else {
                    finish()
                }
            }
        }

        // 시계를 수정할 때와 시계를 생성할 때가 다름
        binding.tvbtnSave.setOnClickListener {
            if (isCreateMode) {
                firebaseAnalytics.logEvent("CREATE_CLOCK") {
                    param("amount_of_clock_part", "${viewModel.getCurrentClockPartAmount()}")
                }
                viewModel.tryInsertClock()
            } else {
                firebaseAnalytics.logEvent("MODIFY_HANDLE", null)
                viewModel.saveModifiedClockData()
            }
        }
    }

    private fun setSeekbar() {
        binding.seekbarHourHandle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setHandWidth(p1, ClockHandAttr.HOUR) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarMinuteHandle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setHandWidth(p1, ClockHandAttr.MINUTE) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarSecondHandle.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setHandWidth(p1, ClockHandAttr.SECOND) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }



    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.clockData.collect { clockData ->
                        binding.viewClockTime.linkClock(clockData)
                        binding.viewbtnHourHandleColor.ivColor.setBackgroundColor(Color.parseColor(clockData.hourHandColor))
                        binding.viewbtnMinuteHandleColor.ivColor.setBackgroundColor(Color.parseColor(clockData.minuteHandColor))
                        binding.viewbtnSecondHandleColor.ivColor.setBackgroundColor(Color.parseColor(clockData.secondHandColor))
                        binding.viewClockTime.invalidate()

                        changeProgress(binding.seekbarHourHandle, clockData.hourHandWidth)
                        changeProgress(binding.seekbarMinuteHandle, clockData.minuteHandWidth)
                        changeProgress(binding.seekbarSecondHandle, clockData.secondHandWidth)
                    }
                }

                launch {
                    viewModel.saveModifiedClockResult.collect {
                        GlobalApplication.isClockDBModified = true
                        if (isCreateMode) {
                            val intent = Intent(this@ClockModifyHandleActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@ClockModifyHandleActivity, BaseActivity::class.java)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }
                }

                launch {
                    viewModel.pickedColor.collect { colorString ->
                        binding.viewColorPicker.setColor(colorString)
                    }
                }
            }
        }
    }

    private fun showColorPicker() {
        binding.viewColorPicker.setColorInUse(ClockData.getColorSet(viewModel.getCurrentClock()))
        binding.viewColorPicker.visibility = View.VISIBLE
        binding.viewColorPicker.animation = getBottomSheetEnterAnimation(binding.viewColorPicker.height.toFloat())
        // colorPicker 가 보여졌을 때 처음으로 표시될 색상 설정
        binding.viewColorPicker.setIsInit()
        viewModel.setColorPickerInitColor()
        binding.viewColorPicker.removeIsInit()
    }

    private fun hideColorPicker() {
        binding.viewColorPicker.animation = getBottomSheetExitAnimation(binding.viewColorPicker.height.toFloat())
        binding.viewColorPicker.visibility = View.GONE
    }

    private fun changeProgress(seekbar : AppCompatSeekBar, newProgress : Int){
        if (seekbar.progress != newProgress) {
            seekbar.progress = newProgress
        }
    }
}