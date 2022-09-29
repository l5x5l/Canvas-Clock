package com.strayAlpaca.data.datasource.room.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "clock_part",
    primaryKeys = ["clockIdx", "clockPartIdx"],
    foreignKeys = [
        ForeignKey(
            entity = ClockEntity::class,
            parentColumns = ["clockIdx"],
            childColumns = ["clockIdx"],
            onDelete = CASCADE
        )
    ]
)
data class ClockPartEntity(
    val clockIdx : Int,
    val clockPartIdx : Int,
    val startAngle : Float,
    val endAngle : Float,
    val firstColor : String,
    val secondColor : String,
    val startRadius : Int,
    val middleRadius : Int,
    val useMiddleRadius : Boolean = true,
    val endRadius : Int,
    val useMiddleLineStroke : Boolean,
    val strokeColor : String,
    val strokeWidth : Int,
    val priority : Int
)