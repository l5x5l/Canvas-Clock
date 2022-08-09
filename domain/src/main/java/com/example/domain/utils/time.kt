package com.example.domain.utils

import java.text.DecimalFormat

fun angleToTime(is24Mode : Boolean = false, angle : Float) : String {
    val hour : String
    val minute : String
    val decimalFormat = DecimalFormat("00")
    if (is24Mode) {
        hour = decimalFormat.format((angle / 15).toInt())
        minute = decimalFormat.format(((angle % 15) * 4).toInt())
    } else {
        hour = decimalFormat.format((angle / 30).toInt())
        minute = decimalFormat.format(((angle % 30) * 2).toInt())
    }
    return "$hour:$minute"
}

fun timeToAngle(is24Mode: Boolean = false, hour : Int, minute : Int) : Float {
    return if (is24Mode) {
        hour * 15 + (minute * 0.25f)
    } else {
        (hour % 12) * 30 + (minute * 0.5f)
    }
}