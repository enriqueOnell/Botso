package com.onell.botso.domain.model

data class Semester(
    val id: Long = 0,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean
)
