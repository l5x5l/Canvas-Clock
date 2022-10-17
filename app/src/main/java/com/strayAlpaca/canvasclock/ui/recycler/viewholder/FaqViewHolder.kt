package com.strayAlpaca.canvasclock.ui.recycler.viewholder

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.strayAlpaca.canvasclock.BuildConfig
import com.strayAlpaca.canvasclock.R
import com.strayAlpaca.canvasclock.databinding.ItemFaqBinding
import com.strayAlpaca.domain.models.FaqData
import com.strayAlpaca.domain.models.ShortCut
import com.strayAlpaca.domain.models.UiStateData

class FaqViewHolder(private val binding : ItemFaqBinding) : RecyclerView.ViewHolder(binding.root) {

    private val uiState = UiStateData()

    init {
        binding.tvbtnShortcut.setOnClickListener {
            if (binding.faqData != null && binding.faqData!!.shortCut == ShortCut.NATIVE_SETTING) {
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID))
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                binding.root.context.startActivity(intent)
            }
        }

        binding.tvFaqTitle.setOnClickListener {
            if (binding.faqData != null) {
                uiState.isSelected = !uiState.isSelected
                setViewState()
            }
        }
    }

    fun bind(faqData : FaqData) {
        binding.faqData = faqData

        setViewState()
    }

    private fun setViewState() {
        if (uiState.isSelected) {
            binding.layoutAnswerArea.visibility = View.VISIBLE
        } else {
            binding.layoutAnswerArea.visibility = View.GONE
        }

        binding.faqData?.let {
            if (it.shortCut != null){
                binding.tvbtnShortcut.visibility = View.VISIBLE
                binding.tvbtnShortcut.text = binding.root.context.getString(R.string.move_to_native_setting)
            } else {
                binding.tvbtnShortcut.visibility = View.GONE
            }
        }
    }
}