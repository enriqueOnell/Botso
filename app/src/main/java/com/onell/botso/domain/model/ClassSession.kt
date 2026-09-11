package com.onell.botso.domain.model

import java.time.LocalTime

data class ClassSession(
    val id: String,
    val courseId: String,
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val room: String = "",
    val isRemote: Boolean = false
)
