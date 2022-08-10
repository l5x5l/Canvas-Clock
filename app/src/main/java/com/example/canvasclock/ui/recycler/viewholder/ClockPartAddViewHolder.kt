package com.example.canvasclock.ui.recycler.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ItemAddClockPartBinding

class ClockPartAddViewHolder(binding : ItemAddClockPartBinding, private val onClick : () -> Unit) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            onClick()
        }
    }
}