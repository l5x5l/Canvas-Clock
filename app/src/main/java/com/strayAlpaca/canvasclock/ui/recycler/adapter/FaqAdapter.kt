package com.strayAlpaca.canvasclock.ui.recycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.databinding.ItemFaqBinding
import com.strayAlpaca.canvasclock.ui.recycler.viewholder.FaqViewHolder
import com.strayAlpaca.domain.models.FaqData

class FaqAdapter : RecyclerView.Adapter<FaqViewHolder>() {

    private var faqList = listOf<FaqData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FaqViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFaqBinding.inflate(inflater, parent, false)
        return FaqViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FaqViewHolder, position: Int) {
        holder.bind(faqList[position])
    }

    override fun getItemCount(): Int = faqList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setFaqList(faqList : List<FaqData>) {
        this.faqList = faqList
        notifyDataSetChanged()
    }
}