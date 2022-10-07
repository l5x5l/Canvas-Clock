package com.strayAlpaca.data.datasource.room.entities

import androidx.room.Entity

@Entity(tableName = "clock_widget", primaryKeys = ["clockWidgetIdx"])
data class ClockWidgetEntity (
    val clockWidgetIdx : Int,
    val clockIdx : Int
)  {
    constructor() : this(0, 0)
}