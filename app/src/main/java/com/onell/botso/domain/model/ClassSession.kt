package com.onell.botso.domain.model

data class ClassSession(
    val id: Long = 0,
    val courseId: Long,
    val dayOfWeek: Int, // 1 (Mon) to 7 (Sun)
    val startTime: String, // HH:mm
    val endTime: String, // HH:mm
    val room: String = ""
)
