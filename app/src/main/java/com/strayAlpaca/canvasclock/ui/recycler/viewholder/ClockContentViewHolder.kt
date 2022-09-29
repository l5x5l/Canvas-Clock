package com.strayAlpaca.canvasclock.ui.recycler.viewholder

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.config.INTENT_KEY_CLOCK
import com.strayAlpaca.canvasclock.databinding.ItemClockContentBinding
import com.strayAlpaca.canvasclock.ui.page.clock_detail.ClockDetailActivity
import com.strayAlpaca.domain.models.ClockData

class ClockContentViewHolder(private val binding : ItemClockContentBinding, private val onClick : ((ClockData) -> Unit) ?= null) : RecyclerView.ViewHolder(binding.root) {

    private var clockData = ClockData()

    init {
        if (onClick == null) {
            binding.viewClockShape.setOnClickListener {
                val intent = Intent(binding.root.context, ClockDetailActivity::class.java)
                intent.putExtra(INTENT_KEY_CLOCK, clockData)
                (binding.root.context).startActivity(intent)
            }
        } else {
            binding.viewClockShape.setOnClickListener {
                onClick.invoke(clockData)
            }
        }
    }

    fun bind(clockData : ClockData) {
        this.clockData = clockData
        binding.viewClockShape.linkClockInfo(clockData.clockPartList)
        binding.viewClockTime.linkClock(clockData)
    }
}