package com.strayAlpaca.canvasclock.models

enum class ClockPartTimeComponent {
    START, END
}

enum class ClockPartColorComponent {
    FIRST, SECOND, STROKE
}

enum class ClockPartPointAttr {
    START, MIDDLE, END, START_TIME, END_TIME, CENTER
}

enum class ClockHandAttr {
    HOUR, MINUTE, SECOND
}

enum class Crud {
    UPDATE, DELETE
}

// 시계 모드에서 색상변경이 가능한 요소들을 의미합니다.
enum class ClockModeEditableComponent {
    TEXT, BACKGROUND
}