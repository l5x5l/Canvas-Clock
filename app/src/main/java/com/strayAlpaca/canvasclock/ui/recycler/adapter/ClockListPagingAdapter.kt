package com.strayAlpaca.canvasclock.ui.recycler.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.strayAlpaca.canvasclock.databinding.ItemClockContentBinding
import com.strayAlpaca.canvasclock.ui.recycler.viewholder.ClockContentViewHolder
import com.strayAlpaca.domain.models.ClockData

class ClockListPagingAdapter(private val itemClick : ((ClockData) -> Unit)? = null) : PagingDataAdapter<ClockData, ClockContentViewHolder>(diffCallback = diffCallback) {

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
        return ClockContentViewHolder(binding, itemClick)
    }
}