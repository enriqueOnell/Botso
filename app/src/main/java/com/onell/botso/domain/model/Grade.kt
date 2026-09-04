package com.onell.botso.domain.model

data class Grade(
    val id: Long = 0,
    val courseId: Long,
    val name: String,
    val score: Double,
    val weight: Double,
    val termId: Int // e.g., 1 for Midterm, 2 for Final
)
