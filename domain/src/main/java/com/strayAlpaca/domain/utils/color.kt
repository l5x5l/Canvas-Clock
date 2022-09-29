package com.strayAlpaca.domain.utils

fun getRed(colorString : String) : String {
    return if (colorString.length == 7) {
        colorString.substring(1, 3)
    } else {
        colorString.substring(3, 5)
    }
}

fun getGreen(colorString : String) : String {
    return if (colorString.length == 7) {
        colorString.substring(3, 5)
    } else {
        colorString.substring(5, 7)
    }
}


fun getBlue(colorString : String) : String {
    return if (colorString.length == 7) {
        colorString.substring(5, 7)
    } else {
        colorString.substring(7, 9)
    }
}

fun getTransparency(colorString : String) : String {
    return if (colorString.length == 7) {
        "FF"
    } else {
        colorString.substring(1, 3)
    }
}

fun isRgbFormat(colorString : String) : Boolean {
    val regex6 = "#[a-fA-F0-9]{6}".toRegex()
    val regex8 = "#[a-fA-F0-9]{8}".toRegex()

    return (colorString.matches(regex6) || colorString.matches(regex8))
}