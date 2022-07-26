package com.example.canvasclock.ui.recycler.viewholder

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ItemClockFooterBinding

class ClockFooterViewHolder(binding : ItemClockFooterBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.root.setOnClickListener {
            Log.d("ClockFooter", "곧 페이지 이동 로직이 추가됩니다!")
        }
    }
}