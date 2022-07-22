package com.example.data.mapper

import com.example.data.datasource.room.entities.ClockEntity
import com.example.data.datasource.room.entities.ClockPartEntity
import com.example.domain.models.ClockData
import com.example.domain.models.ClockPartData

object Mapper {
    fun toClockEntity(clock : ClockData) : ClockEntity {
        return ClockEntity(
            clockIdx = clock.clockIdx,
            hourHandColor = clock.hourHandColor, hourHandWidth = clock.hourHandWidth,
            minuteHandColor = clock.minuteHandColor, minuteHandWidth = clock.minuteHandWidth,
            secondHandColor = clock.secondHandColor, secondHandWidth = clock.secondHandWidth,
            lastModifiedTime = clock.lastModifiedTime
        )
    }

    fun toClockData(clockEntity: ClockEntity, clockPartEntityList : List<ClockPartEntity>) : ClockData {
        val clockPartList = arrayListOf<ClockPartData>()
        for (clockPartEntity in clockPartEntityList) {
            clockPartList.add(toClockPartData(clockPartEntity))
        }

        return ClockData(
            clockIdx = clockEntity.clockIdx,
            hourHandColor = clockEntity.hourHandColor, hourHandWidth = clockEntity.hourHandWidth,
            minuteHandColor = clockEntity.minuteHandColor, minuteHandWidth = clockEntity.minuteHandWidth,
            secondHandColor = clockEntity.secondHandColor, secondHandWidth = clockEntity.secondHandWidth,
            clockPartList = clockPartList,
            lastModifiedTime = clockEntity.lastModifiedTime
        )
    }

    fun toClockPartEntity(clockPart : ClockPartData) : ClockPartEntity {
        return ClockPartEntity(
            clockIdx = clockPart.clockIdx,
            clockPartIdx = clockPart.clockPartIdx,
            startAngle = clockPart.startAngle,
            endAngle = clockPart.endAngle,
            firstColor = clockPart.firstColor,
            secondColor = clockPart.secondColor,
            startRadius = clockPart.startRadius,
            middleRadius = clockPart.middleRadius,
            endRadius = clockPart.endRadius,
            useMiddleLineStroke = clockPart.useMiddleLineStroke,
            strokeColor = clockPart.strokeColor,
            strokeWidth = clockPart.strokeWidth,
            priority = clockPart.priority,
        )
    }

    private fun toClockPartData(clockPartEntity : ClockPartEntity) : ClockPartData {
        return ClockPartData(
            clockIdx = clockPartEntity.clockIdx,
            clockPartIdx = clockPartEntity.clockPartIdx,
            startAngle = clockPartEntity.startAngle,
            endAngle = clockPartEntity.endAngle,
            firstColor = clockPartEntity.firstColor,
            secondColor = clockPartEntity.secondColor,
            startRadius = clockPartEntity.startRadius,
            middleRadius = clockPartEntity.middleRadius,
            endRadius = clockPartEntity.endRadius,
            useMiddleLineStroke = clockPartEntity.useMiddleLineStroke,
            strokeColor = clockPartEntity.strokeColor,
            strokeWidth = clockPartEntity.strokeWidth,
            priority = clockPartEntity.priority,
        )
    }
}