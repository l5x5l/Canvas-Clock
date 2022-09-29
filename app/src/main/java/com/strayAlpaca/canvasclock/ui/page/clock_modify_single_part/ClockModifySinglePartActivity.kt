package com.strayAlpaca.canvasclock.ui.page.clock_modify_single_part

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.config.ADD_MODE
import com.strayAlpaca.canvasclock.config.BaseActivity
import com.strayAlpaca.canvasclock.databinding.ActivityClockModifySinglePartBinding
import com.strayAlpaca.canvasclock.models.ClockPartColorComponent
import com.strayAlpaca.canvasclock.models.ClockPartTimeComponent
import com.strayAlpaca.canvasclock.util.getBottomSheetEnterAnimation
import com.strayAlpaca.canvasclock.util.getBottomSheetExitAnimation
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.utils.angleToTime
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class ClockModifySinglePartActivity : BaseActivity<ActivityClockModifySinglePartBinding>(R.layout.activity_clock_modify_single_part) {
    private val viewModel : ClockModifySinglePartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isAddMode = intent.getBooleanExtra(ADD_MODE, false)
        viewModel.initViewModel(isAddMode = isAddMode)

        setObserver()
        setSeekbar()
        setButton()
        setEditText()

        if (viewModel.selectedClockPartAmount() >= 2) {
            binding.tvbtnStartTime.isEnabled = false
            binding.tvbtnEndTime.isEnabled = false
            binding.tvCannotChangeTime.visibility = View.VISIBLE
            binding.viewClockShape.setMultipleModifyMode()
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
        binding.viewTimePicker.attachChangeCallbackFunction(viewModel::setTimeAngle)


        binding.viewClockShape.linkClockInfo(viewModel.getCurrentClockPart())
        binding.viewClockShape.initModifyAction(setStartRadius = viewModel::setStartRadius, setMiddleRadius = viewModel::setMiddleRadius, setEndRadius = viewModel::setEndRadius, setTimeAngle = viewModel::setTimeAngle, saveTimeAngle = viewModel::saveToMiddle)
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

                        binding.cbUseMiddleLine.isChecked = clockPartAttr.useMiddleLineStroke

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

    private fun setEditText() {
        binding.etStartPoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val number = p0.toString().toIntOrNull()
                number?.let {
                    if (it in 0..100) {
                        viewModel.setStartRadius(number)
                    } else {
                        binding.etStartPoint.setText("100")
                    }
                }
            }
        })

        binding.etMiddlePoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val number = p0.toString().toIntOrNull()
                number?.let {
                    if (it in 0..100) {
                        viewModel.setMiddleRadius(number)
                    } else {
                        binding.etMiddlePoint.setText("100")
                    }
                }
            }
        })

        binding.etEndPoint.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val number = p0.toString().toIntOrNull()
                number?.let {
                    if (it in 0..100) {
                        viewModel.setEndRadius(number)
                    } else {
                        binding.etEndPoint.setText("100")
                    }
                }
            }
        })

        binding.etStrokeWidth.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val number = p0.toString().toIntOrNull()
                number?.let {
                    if (it in 0..100) {
                        viewModel.setStrokeWidth(number)
                    } else {
                        binding.etStrokeWidth.setText("100")
                    }
                }
            }
        })
    }

    override fun setButton() {
        binding.ivbtnBack.setOnClickListener {
            finish()
        }

        binding.tvbtnSave.setOnClickListener {
            viewModel.saveModifiedContent()
            val intent = Intent(this, BaseActivity::class.java)
            setResult(RESULT_OK, intent)
            finish()
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
            binding.viewTimePicker.setTargetTimeComponent(ClockPartTimeComponent.START)
            binding.viewTimePicker.setDate(viewModel.changedClockPartAttr.value.startAngle)
            showTimePicker()
        }

        binding.tvbtnEndTime.setOnClickListener {
            binding.viewTimePicker.setTargetTimeComponent(ClockPartTimeComponent.END)
            binding.viewTimePicker.setDate(viewModel.changedClockPartAttr.value.endAngle)
            showTimePicker()
        }

        binding.cbNotUseMiddlePoint.setOnClickListener { checkBox ->
            checkBox as AppCompatCheckBox
            viewModel.setUseMiddleRadius(!checkBox.isChecked)
        }

        binding.cbUseMiddleLine.setOnClickListener { checkBox ->
            checkBox as AppCompatCheckBox
            viewModel.setUseMiddleLineStroke(checkBox.isChecked)
        }
    }

    // 함수 분리 필요
    private fun showColorPicker() {
        binding.tvbtnSave.isEnabled = false
        binding.ivbtnBack.isEnabled = false
        binding.viewColorPicker.setColorInUse(ClockData.getColorSet(viewModel.getCurrentClock()))
        binding.viewColorPicker.visibility = View.VISIBLE
        binding.viewColorPicker.animation = getBottomSheetEnterAnimation(binding.viewColorPicker.height.toFloat())
        // colorPicker 가 보여졌을 때 처음으로 표시될 색상 설정
        binding.viewColorPicker.setIsInit()
        viewModel.setColorPickerInitColor()
        binding.viewColorPicker.removeIsInit()
    }

    private fun hideColorPicker() {
        binding.tvbtnSave.isEnabled = true
        binding.ivbtnBack.isEnabled = true
        binding.viewColorPicker.animation = getBottomSheetExitAnimation(binding.viewColorPicker.height.toFloat())
        binding.viewColorPicker.visibility = View.GONE
    }

    private fun changeColor(colorString : String) {
        viewModel.setCurrentColor(colorString)
    }

    private fun showTimePicker(){
        binding.nestedTimePicker.animation = getBottomSheetEnterAnimation(binding.nestedTimePicker.height.toFloat())
        binding.nestedTimePicker.visibility = View.VISIBLE
        binding.viewClockShape.setTimeIntervalChangeButtonEnable(enabled = false)
    }

    private fun hideTimePicker(){
        binding.nestedTimePicker.animation = getBottomSheetExitAnimation(binding.nestedTimePicker.height.toFloat())
        binding.nestedTimePicker.visibility = View.GONE
        binding.viewClockShape.setTimeIntervalChangeButtonEnable(enabled = true)
    }
}