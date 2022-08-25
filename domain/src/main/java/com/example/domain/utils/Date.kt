package com.example.domain.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * 현재 시간을 YYYYMMddHHmmss 형식으로 반환합니다
 */
fun getCurrentTime() : String {
    val currentDate = Date()
    val simpleDateFormat = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
    return simpleDateFormat.format(currentDate)
}