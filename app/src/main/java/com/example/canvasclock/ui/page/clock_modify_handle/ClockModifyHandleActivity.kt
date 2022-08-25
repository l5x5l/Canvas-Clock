package com.example.canvasclock.ui.page.clock_modify_handle

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
import com.example.canvasclock.R
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.config.GlobalApplication
import com.example.canvasclock.databinding.ActivityClockModifyHandleBinding
import com.example.canvasclock.models.ClockHandAttr
import com.example.canvasclock.ui.custom_components.TwoButtonDialog
import com.example.domain.models.ClockData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ClockModifyHandleActivity : BaseActivity<ActivityClockModifyHandleBinding>(R.layout.activity_clock_modify_handle) {
    private val viewModel : ClockModifyHandleViewModel by viewModels()
    private val confirmDialog : TwoButtonDialog by lazy { TwoButtonDialog() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setButton()
        setObserver()
        setSeekbar()

        binding.viewClockShape.linkClockInfo(viewModel.clockData.value.clockPartList)

        confirmDialog.setFirstButton(buttonText = R.string.go_back)
        confirmDialog.setSecondButton(buttonText = R.string.cancel, buttonClickEvent = {
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

        binding.ivbtnBack.setOnClickListener {
            if (viewModel.isChanged()) {
                confirmDialog.show(supportFragmentManager, "ConfirmDialog")
            } else {
                finish()
            }
        }

        binding.tvbtnSave.setOnClickListener {
            viewModel.saveModifiedClockData()
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
                    viewModel.saveModifiedClockResult.collect { _ ->
                        GlobalApplication.isClockDBModified = true
                        val intent = Intent(this@ClockModifyHandleActivity, BaseActivity::class.java)
                        setResult(RESULT_OK, intent)
                        finish()
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
        binding.nestedColorPicker.visibility = View.VISIBLE
        binding.viewColorPicker.setIsInit()
        viewModel.setColorPickerInitColor()
        binding.viewColorPicker.removeIsInit()
    }

    private fun hideColorPicker() {
        binding.nestedColorPicker.visibility = View.GONE
    }

    private fun changeProgress(seekbar : AppCompatSeekBar, newProgress : Int){
        if (seekbar.progress != newProgress) {
            seekbar.progress = newProgress
        }
    }
}