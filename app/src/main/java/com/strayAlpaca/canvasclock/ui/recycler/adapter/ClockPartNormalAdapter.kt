package com.strayAlpaca.canvasclock.ui.recycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemClockPartNormalBinding
import com.strayAlpaca.canvasclock.ui.recycler.viewholder.ClockPartNormalViewHolder
import com.strayAlpaca.domain.models.ClockPartData

class ClockPartNormalAdapter : RecyclerView.Adapter<ClockPartNormalViewHolder>() {

    private val clockPart : ArrayList<ClockPartData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockPartNormalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClockPartNormalBinding.inflate(inflater, parent, false)
        return ClockPartNormalViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClockPartNormalViewHolder, position: Int) {
        holder.bind(clockPartData = clockPart[position])
    }

    override fun getItemCount(): Int = clockPart.size

    @SuppressLint("NotifyDataSetChanged")
    fun setClockPart(newClockPartData: ArrayList<ClockPartData>) {
        clockPart.clear()
        clockPart.addAll(newClockPartData)
        notifyDataSetChanged()
    }
}