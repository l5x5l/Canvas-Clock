package com.example.canvasclock.ui.recycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ItemClockContentBinding
import com.example.domain.models.ClockData

class ClockContentViewHolder(private val binding : ItemClockContentBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        // click event 등록
    }

    fun bind(clockData : ClockData) {
        binding.viewClockShape.linkClockInfo(clockData.clockPartList)
        binding.viewClockTime.linkClock(clockData)
    }
}