package com.strayAlpaca.canvasclock.ui.custom_components

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.strayAlpaca.canvasclock.databinding.ViewAdmobInClockModeBinding

class AdmobInClockModeView(context : Context, attrs : AttributeSet) : FrameLayout(context, attrs)  {
    private val binding = ViewAdmobInClockModeBinding.inflate(LayoutInflater.from(context), this, true)
    var currentNativeAd : NativeAd ?= null

    fun loadAd(adUnitId : String) {
        val builder = AdLoader.Builder(context, adUnitId)
        builder.forNativeAd { nativeAd ->
            if (checkIsDestroy()) {
                nativeAd.destroy()
                return@forNativeAd
            }
            currentNativeAd?.destroy()
            currentNativeAd = nativeAd

            binding.adMain.visibility = View.VISIBLE
            setAdUi(nativeAd)
        }

        val adLoader = builder.withAdListener(
            object : AdListener() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    binding.adMain.visibility = View.GONE
                }
            }
        ).withNativeAdOptions(
            NativeAdOptions.Builder()
                .setAdChoicesPlacement(NativeAdOptions.ADCHOICES_TOP_LEFT)
                .setRequestCustomMuteThisAd(true)
                .build()
        ).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun setAdUi(nativeAd : NativeAd) {
        val nativeAdView = binding.adMain
        nativeAdView.headlineView = binding.tvAdmobMain
        nativeAdView.bodyView = binding.tvAdmobDescription
        nativeAdView.iconView = binding.ivAdmobImg

        binding.tvAdmobMain.text = nativeAd.headline
        nativeAd.icon?.let { icon ->
            binding.ivAdmobImg.setImageDrawable(icon.drawable)
        }

        if (nativeAd.body == null) {
            binding.tvAdmobDescription.visibility = View.GONE
        } else {
            binding.tvAdmobDescription.text = nativeAd.body
        }

        nativeAdView.setNativeAd(nativeAd)
    }

    private fun checkIsDestroy( ) : Boolean {
        return (context as Activity).isDestroyed || (context as Activity).isFinishing || (context as Activity).isChangingConfigurations
    }

    fun removeAd() {
        currentNativeAd?.destroy()
    }
}