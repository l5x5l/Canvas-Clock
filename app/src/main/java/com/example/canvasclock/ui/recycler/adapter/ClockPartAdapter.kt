package com.example.canvasclock.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ItemAddClockPartBinding
import com.example.canvasclock.databinding.ItemClockPartModifyBinding
import com.example.canvasclock.ui.recycler.viewholder.ClockPartAddViewHolder
import com.example.canvasclock.ui.recycler.viewholder.ClockPartModifyViewHolder
import com.example.domain.models.ClockPartData

class ClockPartAdapter(private val addClickEvent : () -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val clockPartList: ArrayList<ClockPartData> = arrayListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == 0) {
            val binding = ItemAddClockPartBinding.inflate(inflater, parent, false)
            ClockPartAddViewHolder(binding, addClickEvent)
        } else {
            val binding = ItemClockPartModifyBinding.inflate(inflater, parent, false)
            ClockPartModifyViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ClockPartModifyViewHolder) {
            holder.bind(clockPartList[position - 1])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) 0
        else 1
    }

    override fun getItemCount(): Int = clockPartList.size + 1

    fun applyClockPartList(clockPartList : ArrayList<ClockPartData>) {
        this.clockPartList.clear()
        this.clockPartList.addAll(clockPartList)
        notifyItemRangeChanged(1, clockPartList.size)
    }
}