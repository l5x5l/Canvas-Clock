package com.example.canvasclock.ui.recycler.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.canvasclock.databinding.ViewColorBinding
import com.example.canvasclock.ui.recycler.viewholder.ColorViewHolder

class ColorInUseAdapter(private val changeColor : (String) -> Unit) : RecyclerView.Adapter<ColorViewHolder>() {

    private val colorInUse = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ViewColorBinding.inflate(inflater, parent, false)
        return ColorViewHolder(binding, changeColor)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colorInUse.elementAt(position))
    }

    override fun getItemCount(): Int = colorInUse.size

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(newColorInUse : MutableSet<String>) {
        colorInUse.clear()
        colorInUse.addAll(newColorInUse)
        notifyDataSetChanged()
    }
}