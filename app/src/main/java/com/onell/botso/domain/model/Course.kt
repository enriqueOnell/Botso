package com.onell.botso.domain.model

data class Course(
    val id: Long = 0,
    val semesterId: Long,
    val name: String,
    val code: String = "",
    val colorHex: String = "",
    val location: String,
    val professor: String,
    val dayOfWeek: Int = 1,
    val startTime: String = "08:00",
    val endTime: String = "10:00",
    val isRemote: Boolean = false
)
