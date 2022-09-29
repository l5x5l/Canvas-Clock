package com.strayAlpaca.canvasclock.ui.recycler.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemClockPartModifyBinding
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.utils.angleToTime

class ClockPartModifyViewHolder(private val binding: ItemClockPartModifyBinding) : RecyclerView.ViewHolder(binding.root) {
    init {
        binding.cb.setOnClickListener {
            if (binding.clockPart != null) {
                binding.layoutContainer.isSelected = binding.cb.isChecked
                binding.clockPart!!.uiState.isSelected = binding.cb.isChecked
            }
            binding.layoutContainer.invalidate()
        }
    }

    @SuppressLint("SetTextI18n")
    fun bind(clockPart : ClockPartData) {
        binding.clockPart = clockPart
        binding.layoutContainer.isSelected = clockPart.uiState.isSelected
        binding.cb.isChecked = clockPart.uiState.isSelected
        binding.viewColor1.ivColor.setBackgroundColor(Color.parseColor(clockPart.firstColor))
        binding.viewColor2.ivColor.setBackgroundColor(Color.parseColor(clockPart.secondColor))
        binding.tvTime.text = angleToTime(is24Mode = true, angle = clockPart.startAngle) + " ~ " + angleToTime(true, clockPart.endAngle)
    }

}