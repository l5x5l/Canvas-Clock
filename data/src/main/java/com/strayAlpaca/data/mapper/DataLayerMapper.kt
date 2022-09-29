package com.strayAlpaca.data.mapper

import com.strayAlpaca.data.datasource.room.entities.ClockEntity
import com.strayAlpaca.data.datasource.room.entities.ClockPartEntity
import com.strayAlpaca.domain.models.ClockData
import com.strayAlpaca.domain.models.ClockPartData

object DataLayerMapper {
    /**
     * ClockData 를 ClockEntity 로 전환합니다.
     * 만약 기존 clockIdx 가 아닌, 새로운 clockIdx 값을 사용하고자 한다면, clockIdx 인자를 특정하면 됩니다.
     */
    fun toClockEntity(clock : ClockData, clockIdx : Int ?= null) : ClockEntity {
        return ClockEntity(
            clockIdx = clockIdx ?: clock.clockIdx,
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

    /**
     * ClockPartData 를 ClockPartEntity 로 전환합니다.
     * 만약 기존 clockIdx 가 아닌, 새로운 clockIdx 값을 사용하고자 한다면, clockIdx 인자를 특정하면 됩니다.
     */
    fun toClockPartEntity(clockPart : ClockPartData, clockIdx : Int ?= null) : ClockPartEntity {
        return ClockPartEntity(
            clockIdx = clockIdx ?: clockPart.clockIdx,
            clockPartIdx = clockPart.clockPartIdx,
            startAngle = clockPart.startAngle,
            endAngle = clockPart.endAngle,
            firstColor = clockPart.firstColor,
            secondColor = clockPart.secondColor,
            startRadius = clockPart.startRadius,
            middleRadius = clockPart.middleRadius,
            useMiddleRadius = clockPart.useMiddleRadius,
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
            useMiddleRadius = clockPartEntity.useMiddleRadius,
            endRadius = clockPartEntity.endRadius,
            useMiddleLineStroke = clockPartEntity.useMiddleLineStroke,
            strokeColor = clockPartEntity.strokeColor,
            strokeWidth = clockPartEntity.strokeWidth,
            priority = clockPartEntity.priority,
        )
    }
}