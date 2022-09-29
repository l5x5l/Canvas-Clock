package com.strayAlpaca.canvasclock.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.strayAlpaca.canvasclock.databinding.ViewTimePickerBinding
import com.strayAlpaca.canvasclock.models.ClockPartTimeComponent
import com.strayAlpaca.domain.utils.angleToTime

class TimePickerView(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = ViewTimePickerBinding.inflate(LayoutInflater.from(context), this, true)
    private var timeComponent : ClockPartTimeComponent = ClockPartTimeComponent.START

    private var isInitSetting = false

    fun setTargetTimeComponent(timeComponent: ClockPartTimeComponent) {
        this.timeComponent = timeComponent
    }

    fun setButtonCallback(cancelCallback : () -> Unit, selectCallback : () -> Unit) {
        binding.tvbtnCancel.setOnClickListener {
            cancelCallback()
        }
        binding.tvbtnSelect.setOnClickListener {
            selectCallback()
        }
    }

    fun setDate(angle : Float) {
        isInitSetting = true
        val time = angleToTime(is24Mode = true, angle).split(":")
        binding.viewTimePicker.hour = time[0].toInt()
        binding.viewTimePicker.minute = time[1].toInt()
        isInitSetting = false
    }


    fun attachChangeCallbackFunction(callback : (Int, Int, ClockPartTimeComponent) -> Unit) {
        binding.viewTimePicker.setOnTimeChangedListener { _, hour, minute ->
            if (!isInitSetting) {
                callback(hour, minute, timeComponent)
            }
        }
    }
}