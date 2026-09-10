package com.onell.botso.domain.model

data class Grade(
    val id: String,
    val courseId: String,
    val name: String,
    val score: Double,
    val weight: Double,
    val termId: Int
)