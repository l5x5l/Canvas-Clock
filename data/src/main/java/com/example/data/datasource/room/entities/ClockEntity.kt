package com.example.data.datasource.room.entities

import androidx.room.Entity

@Entity(tableName = "clock", primaryKeys = ["clockIdx"])
data class ClockEntity(
    val clockIdx : Int,
    val hourHandColor : String,
    val hourHandWidth : Int,
    val minuteHandColor : String,
    val minuteHandWidth : Int,
    val secondHandColor : String,
    val secondHandWidth : Int,
    val lastModifiedTime : String
)