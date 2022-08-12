package com.example.canvasclock.ui.page.clock_modify_single_part

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.canvasclock.R
import com.example.canvasclock.config.ADD_MODE
import com.example.canvasclock.config.BaseActivity
import com.example.canvasclock.databinding.ActivityClockModifySinglePartBinding
import com.example.canvasclock.models.ClockPartColorComponent
import com.example.canvasclock.models.ClockPartTimeComponent
import com.example.domain.models.ClockData
import com.example.domain.utils.angleToTime
import kotlinx.coroutines.launch

class ClockModifySinglePartActivity : BaseActivity<ActivityClockModifySinglePartBinding>(R.layout.activity_clock_modify_single_part) {
    private val viewModel : ClockModifySinglePartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAddMode = intent.getBooleanExtra(ADD_MODE, false)
        viewModel.initViewModel(isAddMode = isAddMode)

        setObserver()
        setSeekbar()
        setButton()

        if (viewModel.selectedClockPartAmount() >= 2) {
            binding.tvbtnStartTime.isEnabled = false
            binding.tvbtnEndTime.isEnabled = false
            binding.tvCannotChangeTime.visibility = View.VISIBLE
        }

        binding.viewColorPicker.setButtonCallback(cancelCallback = {
            hideColorPicker()
            viewModel.rollbackColor()
        }, selectCallback = {
            hideColorPicker()
            viewModel.saveToMiddle()
        })
        binding.viewColorPicker.attachChangeCallbackFunction(::changeColor)

        binding.viewTimePicker.setButtonCallback(cancelCallback = {
            hideTimePicker()
            viewModel.rollbackTime()
        }, selectCallback = {
            hideTimePicker()
            viewModel.saveToMiddle()
        })
        binding.viewTimePicker.attachChangeCallbackFunction(::changeTime)


        binding.viewClockShape.linkClockInfo(viewModel.getCurrentClockPart())
    }

    private fun setObserver(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.changedClockPartAttr.collect { clockPartAttr ->
                        changeProgress(binding.seekbarStartPoint, clockPartAttr.startRadius)
                        changeProgressText(binding.etStartPoint, clockPartAttr.startRadius)

                        changeProgress(binding.seekbarEndPoint, clockPartAttr.endRadius)
                        changeProgressText(binding.etEndPoint, clockPartAttr.endRadius)

                        changeProgress(binding.seekbarStrokeWidth, clockPartAttr.strokeWidth)
                        changeProgressText(binding.etStrokeWidth, clockPartAttr.strokeWidth)

                        if (!clockPartAttr.useMiddleRadius) {
                            binding.cbNotUseMiddlePoint.isChecked = true
                            binding.seekbarMiddlePoint.isEnabled = false
                            binding.etMiddlePoint.isEnabled = false
                        } else {
                            binding.cbNotUseMiddlePoint.isChecked = false
                            binding.seekbarMiddlePoint.isEnabled = true
                            binding.etMiddlePoint.isEnabled = true
                            changeProgress(binding.seekbarMiddlePoint, clockPartAttr.middleRadius)
                            changeProgressText(binding.etMiddlePoint, clockPartAttr.middleRadius)
                        }

                        binding.tvbtnStartTime.text = angleToTime(is24Mode = true, clockPartAttr.startAngle)
                        binding.tvbtnEndTime.text = angleToTime(is24Mode = true, clockPartAttr.endAngle)

                        binding.viewbtnColor1.ivColor.setBackgroundColor(Color.parseColor(clockPartAttr.firstColor))
                        binding.viewbtnColor2.ivColor.setBackgroundColor(Color.parseColor(clockPartAttr.secondColor))
                        binding.viewbtnStrokeColor.ivColor.setBackgroundColor(Color.parseColor(clockPartAttr.strokeColor))

                        // 상세 수정 필요
                        binding.viewClockShape.invalidateClock()
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

    private fun changeProgress(seekbar : AppCompatSeekBar, newProgress : Int){
        if (seekbar.progress != newProgress) {
            seekbar.progress = newProgress
        }
    }

    private fun changeProgressText(editText : AppCompatEditText, newProgress : Int) {
        if (editText.text.toString().toIntOrNull() != newProgress) {
            editText.setText(newProgress.toString())
        }
    }

    private fun setSeekbar(){
        binding.seekbarStartPoint.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setStartRadius(p1) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarMiddlePoint.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setMiddleRadius(p1) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarEndPoint.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setEndRadius(p1) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })

        binding.seekbarStrokeWidth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) { viewModel.setStrokeWidth(p1) }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {

        }

        binding.viewbtnColor1.root.setOnClickListener {
            viewModel.pickedColorComponent = ClockPartColorComponent.FIRST
            showColorPicker()
        }

        binding.viewbtnColor2.root.setOnClickListener {
            viewModel.pickedColorComponent = ClockPartColorComponent.SECOND
            showColorPicker()
        }

        binding.viewbtnStrokeColor.root.setOnClickListener {
            viewModel.pickedColorComponent = ClockPartColorComponent.STROKE
            showColorPicker()
        }

        binding.tvbtnStartTime.setOnClickListener {
            viewModel.pickedTimeComponent = ClockPartTimeComponent.START
            binding.viewTimePicker.setDate(viewModel.changedClockPartAttr.value.startAngle)
            showTimePicker()
        }

        binding.tvbtnEndTime.setOnClickListener {
            viewModel.pickedTimeComponent = ClockPartTimeComponent.END
            binding.viewTimePicker.setDate(viewModel.changedClockPartAttr.value.endAngle)
            showTimePicker()
        }

        binding.cbNotUseMiddlePoint.setOnClickListener { checkBox ->
            checkBox as AppCompatCheckBox
            viewModel.setUseMiddleRadius(!checkBox.isChecked)
        }
    }

    // 함수 분리 필요
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

    private fun changeColor(colorString : String) {
        viewModel.setCurrentColor(colorString)
    }

    private fun showTimePicker(){
        binding.nestedTimePicker.visibility = View.VISIBLE
    }

    private fun hideTimePicker(){
        binding.nestedTimePicker.visibility = View.GONE
    }

    private fun changeTime(hour : Int, minute : Int) {
        viewModel.setTimeAngle(hour = hour, minute = minute)
    }
}