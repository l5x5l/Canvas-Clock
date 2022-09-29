package com.strayAlpaca.canvasclock.ui.recycler.viewholder

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemClockFooterBinding
import com.strayAlpaca.canvasclock.ui.page.clock_list.ClockListActivity

class ClockFooterViewHolder(binding : ItemClockFooterBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, ClockListActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }
}