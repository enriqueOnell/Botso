package com.onell.botso.domain.model

data class ClassSession(
    val id: String,
    val courseId: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val room: String = ""
)
