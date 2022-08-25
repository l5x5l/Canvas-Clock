package com.example.canvasclock.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.canvasclock.databinding.ItemClockContentBinding
import com.example.canvasclock.ui.recycler.viewholder.ClockContentViewHolder
import com.example.domain.models.ClockData

class ClockListPagingAdapter : PagingDataAdapter<ClockData, ClockContentViewHolder>(diffCallback = diffCallback) {

    // domain 레이어에 DiffUtil 을 사용한 diffCallback 을 사용해서는 안되기에, 이 부분에 선언
    // (diffCallback 을 별도의 폴더로 빼는 것도 고려중)
    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ClockData>() {
            override fun areItemsTheSame(oldItem: ClockData, newItem: ClockData): Boolean {
                return (oldItem.clockIdx == newItem.clockIdx)
            }

            override fun areContentsTheSame(oldItem: ClockData, newItem: ClockData): Boolean {
                return (oldItem.lastModifiedTime == newItem.lastModifiedTime)
            }
        }
    }

    override fun onBindViewHolder(holder: ClockContentViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockContentViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemClockContentBinding.inflate(inflater, parent, false)
        return ClockContentViewHolder(binding)
    }

    fun applyChange(position : Int, clock : ClockData) {
        getItem(position)?.let { prevClock ->
            prevClock.clockPartList = clock.clockPartList
            prevClock.lastModifiedTime = clock.lastModifiedTime
            prevClock.hourHandColor = clock.hourHandColor
            prevClock.hourHandWidth = clock.hourHandWidth
            prevClock.minuteHandColor = clock.minuteHandColor
            prevClock.minuteHandColor = clock.minuteHandColor
            prevClock.secondHandColor = clock.secondHandColor
            prevClock.secondHandWidth = clock.secondHandWidth
            notifyItemChanged(position)
        }
    }
}