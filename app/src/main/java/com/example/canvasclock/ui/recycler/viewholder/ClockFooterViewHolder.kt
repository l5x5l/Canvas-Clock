package com.example.canvasclock.ui.recycler.viewholder

import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ItemClockFooterBinding
import com.example.canvasclock.ui.page.clock_list.ClockListActivity

class ClockFooterViewHolder(binding : ItemClockFooterBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            val intent = Intent(binding.root.context, ClockListActivity::class.java)
            binding.root.context.startActivity(intent)
        }
    }
}