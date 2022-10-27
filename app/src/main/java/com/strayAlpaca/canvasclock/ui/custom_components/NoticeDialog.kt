package com.strayAlpaca.canvasclock.ui.custom_components

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.fragment.app.DialogFragment
import com.strayAlpaca.canvasclock.databinding.DialogNoticeBinding

class NoticeDialog : DialogFragment() {
    private var _binding : DialogNoticeBinding ?= null
    private val binding get() = _binding!!

    @StringRes private var titleRes : Int? = null
    @StringRes private var bodyTextRes : Int? = null

    @DrawableRes private var imageRes : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogNoticeBinding.inflate(layoutInflater)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.setCanceledOnTouchOutside(false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        titleRes?.let { binding.tvDialogTitle.text = getString(it) }
        bodyTextRes?.let { binding.tvDialogBody.text = getString(it) }
        imageRes?.let { binding.ivBody.setImageResource(it) }

        binding.ivbtnClose.setOnClickListener {
            dismiss()
        }
    }

    fun setDialogData(@StringRes titleResId : Int? = null, @StringRes bodyResId : Int? = null, @DrawableRes imageResId : Int? = null) {
        titleRes = titleResId
        bodyTextRes = bodyResId
        imageRes = imageResId
    }
}