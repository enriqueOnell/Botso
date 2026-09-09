package com.onell.botso.domain.model

data class Semester(
    val id: String,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val isActive: Boolean
)
