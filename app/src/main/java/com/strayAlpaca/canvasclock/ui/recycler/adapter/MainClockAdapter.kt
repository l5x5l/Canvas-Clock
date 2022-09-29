package com.strayAlpaca.canvasclock.ui.recycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemClockContentBinding
import com.strayAlpaca.canvasclock.databinding.ItemClockFooterBinding
import com.strayAlpaca.canvasclock.ui.recycler.viewholder.ClockContentViewHolder
import com.strayAlpaca.canvasclock.ui.recycler.viewholder.ClockFooterViewHolder
import com.strayAlpaca.domain.models.ClockData
import kotlin.math.min

class MainClockAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val clockList = ArrayList<ClockData>()

    override fun getItemViewType(position: Int): Int {
        return if (position == clockList.size) {
            1
        } else {
            0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = ItemClockContentBinding.inflate(inflater, parent, false)
            ClockContentViewHolder(binding)
        } else {
            val binding = ItemClockFooterBinding.inflate(inflater, parent, false)
            ClockFooterViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClockContentViewHolder) {
            holder.bind(clockList[position])
        }
    }

    override fun getItemCount(): Int = min(clockList.size + 1, 6)

    @SuppressLint("NotifyDataSetChanged")
    fun setClockList(newClockList : ArrayList<ClockData>) {
        clockList.clear()
        clockList.addAll(newClockList)
        notifyDataSetChanged()
    }
}