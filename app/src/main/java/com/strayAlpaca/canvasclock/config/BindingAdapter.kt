package com.strayAlpaca.canvasclock.config

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.models.Time
import com.strayAlpaca.canvasclock.ui.custom_components.ClockTimerView
import com.strayAlpaca.canvasclock.ui.recycler.decoration.*

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("time")
    fun setClockViewTime(view : ClockTimerView, time : Time?) {
        val useTime = time ?: Time()
        view.setTime(hour = useTime.hour, minute = useTime.minute, second = useTime.second)
        view.invalidate()
    }

    @JvmStatic
    @BindingAdapter("use24HourMode")
    fun set24HourMode(view : ClockTimerView, is24HourMode : Boolean?){
        if (is24HourMode !== null) {
            view.change24HourMode(is24HourMode)
            view.invalidate()
        }
    }

    @JvmStatic
    @BindingAdapter("gridLayoutManager")
    fun setGridLayoutManager(view : RecyclerView, grid : Int) {
        view.layoutManager = GridLayoutManager(view.context, grid)
        view.addItemDecoration(GridDecoration(context = view.context, gridColumn = grid))
    }

    @JvmStatic
    @BindingAdapter("ViewportHeight")
    fun setViewportHeight(view : View, height : Float) {
        view.layoutParams = view.layoutParams.apply {
            val windowMetrics = view.context.resources.displayMetrics
            this.height = (windowMetrics.heightPixels * height).toInt()
        }
    }
}