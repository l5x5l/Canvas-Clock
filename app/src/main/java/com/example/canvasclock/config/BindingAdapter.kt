package com.example.canvasclock.config

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.models.Time
import com.example.canvasclock.ui.custom_components.ClockTimerView
import com.example.canvasclock.ui.recycler.decoration.Grid2Decoration
import com.example.canvasclock.ui.recycler.decoration.Grid3Decoration
import com.example.canvasclock.ui.recycler.decoration.Grid4Decoration
import com.example.canvasclock.ui.recycler.decoration.Grid6Decoration

object BindingAdapter {
    @JvmStatic
    @BindingAdapter("time")
    fun setClockViewTime(view : ClockTimerView, time : Time?) {
        val useTime = time ?: Time()
        view.setTime(hour = useTime.hour, minute = useTime.minute, second = useTime.second)
        view.invalidate()
    }

    @JvmStatic
    @BindingAdapter("gridLayoutManager")
    fun setGridLayoutManager(view : RecyclerView, grid : Int) {
        view.layoutManager = GridLayoutManager(view.context, grid)
        when (grid) {
            2 -> {
                view.addItemDecoration(Grid2Decoration(view.context))
            }
            3 -> {
                view.addItemDecoration(Grid3Decoration(view.context))
            }
            4 -> {
                view.addItemDecoration(Grid4Decoration(view.context))
            }
            6 -> {
                view.addItemDecoration(Grid6Decoration(view.context))
            }
        }
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