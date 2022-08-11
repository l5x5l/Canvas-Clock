package com.example.canvasclock.ui.custom_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.canvasclock.databinding.ViewTimePickerBinding
import com.example.domain.utils.angleToTime

class TimePickerView(context : Context, attrs : AttributeSet) : ConstraintLayout(context, attrs) {
    private val binding = ViewTimePickerBinding.inflate(LayoutInflater.from(context), this, true)

    private var isInitSetting = false

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


    fun attachChangeCallbackFunction(callback : (Int, Int) -> Unit) {
        binding.viewTimePicker.setOnTimeChangedListener { _, hour, minute ->
            if (!isInitSetting) {
                callback(hour, minute)
            }
        }
    }
}