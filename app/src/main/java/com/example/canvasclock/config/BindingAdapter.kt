package com.example.canvasclock.config

import androidx.databinding.BindingAdapter
import com.example.canvasclock.models.Time
import com.example.canvasclock.ui.custom_components.ClockTimerView

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("time")
    fun setClockViewTime(view : ClockTimerView, time : Time?) {
        val useTime = time ?: Time()
        view.setTime(hour = useTime.hour, minute = useTime.minute, second = useTime.second)
        view.invalidate()
    }


}