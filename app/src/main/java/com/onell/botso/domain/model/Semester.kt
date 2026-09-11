package com.onell.botso.domain.model

import java.time.LocalDate

data class Semester(
    val id: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isActive: Boolean = true
)
