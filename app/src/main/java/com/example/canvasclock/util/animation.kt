package com.example.canvasclock.util

import android.view.animation.AlphaAnimation
import android.view.animation.TranslateAnimation

fun getBottomSheetEnterAnimation(height : Float) : TranslateAnimation {
    val anim = TranslateAnimation(0f, 0f, height, 0f)
    anim.duration = 350
    anim.fillAfter = true

    return anim
}

fun getBottomSheetExitAnimation(height : Float) : TranslateAnimation{
    val anim = TranslateAnimation(0f, 0f, 0f, height)
    anim.duration = 350
    anim.fillAfter = false

    return anim
}

fun getFadeInAnimation() : AlphaAnimation {
    val anim = AlphaAnimation(0f, 1f)
    anim.duration = 350
    anim.fillAfter = true

    return anim
}

fun getFadeOutAnimation() : AlphaAnimation {
    val anim = AlphaAnimation(1f, 0f)
    anim.duration = 350
    anim.fillAfter = false

    return anim
}