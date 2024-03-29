package com.strayAlpaca.canvasclock.ui.recycler.viewholder

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemClockPartNormalBinding
import com.strayAlpaca.domain.models.ClockPartData
import com.strayAlpaca.domain.utils.angleToTime

class ClockPartNormalViewHolder(private val binding : ItemClockPartNormalBinding) : RecyclerView.ViewHolder(binding.root) {
    @SuppressLint("SetTextI18n")
    fun bind(clockPartData: ClockPartData) {
        binding.tvTime.text = angleToTime(is24Mode = false, clockPartData.startAngle) + " ~ " + angleToTime(is24Mode = false, clockPartData.endAngle)
        binding.viewColor1.ivColor.setBackgroundColor(Color.parseColor(clockPartData.firstColor))
        binding.viewColor2.ivColor.setBackgroundColor(Color.parseColor(clockPartData.secondColor))
    }
}