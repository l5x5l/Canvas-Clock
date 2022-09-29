package com.strayAlpaca.domain.models

data class CoordinateClockPartData(
    val startCoordinate : Coordinate,
    val middleCoordinate : Coordinate,
    val endLeftCoordinate : Coordinate,
    val endRightCoordinate : Coordinate,
    val firstColor : String,
    val secondColor : String,
    val uiStateData: UiStateData,
    val strokeColor : String,
    val strokeWidth : Float,
    val useMiddleLineStroke : Boolean
)
