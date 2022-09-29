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
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.strayAlpaca.canvasclock.databinding.Dialog2ButtonsBinding

class TwoButtonDialog : DialogFragment() {
    private var _binding : Dialog2ButtonsBinding ?= null
    private val binding get() = _binding!!

    private var button1Event : (() -> Unit) ?= null
    private var button2Event : (() -> Unit) ?= null

    @StringRes private var button1Text : Int? = null
    @StringRes private var button2Text : Int? = null
    @StringRes private var mainMessageText : Int? = null

    @DrawableRes private var button1Background : Int? = null
    @DrawableRes private var button2Background : Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = Dialog2ButtonsBinding.inflate(layoutInflater)
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

        button1Text?.let { binding.tvbtn1.text = getString(it) }
        button2Text?.let { binding.tvbtn2.text = getString(it) }
        mainMessageText?.let { binding.tvDialogMessage.text = getString(it) }

        button1Background?.let { binding.tvbtn1.background = ContextCompat.getDrawable(requireContext(), it) }
        button2Background?.let { binding.tvbtn2.background = ContextCompat.getDrawable(requireContext(), it) }

        binding.tvbtn1.setOnClickListener {
            button1Event?.let { it() }
            dismiss()
        }

        binding.tvbtn2.setOnClickListener {
            button2Event?.let { it() }
            dismiss()
        }
    }

    fun setFirstButton(@StringRes buttonText : Int, buttonClickEvent : (() -> Unit)? = null, @DrawableRes buttonStyle: Int? = null) {
        button1Text = buttonText
        button1Event = buttonClickEvent
        button1Background = buttonStyle
    }

    fun setSecondButton(@StringRes buttonText : Int, buttonClickEvent : (() -> Unit)? = null, @DrawableRes buttonStyle : Int? = null){
        button2Text = buttonText
        button2Event = buttonClickEvent
        button2Background = buttonStyle
    }

    fun setMainMessage(@StringRes messageText : Int) {
        mainMessageText = messageText
    }
}