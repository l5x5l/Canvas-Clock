package com.example.canvasclock.ui.recycler.viewholder

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ViewColorBinding

class ColorViewHolder(private val binding : ViewColorBinding, onclick : (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

    private var color : String = "#FFFFFFFF"

    init {
        binding.root.setOnClickListener {
            onclick(color)
        }
    }

    fun bind(colorString : String) {
        color = colorString
        binding.ivColor.setBackgroundColor(Color.parseColor(color))
    }
}