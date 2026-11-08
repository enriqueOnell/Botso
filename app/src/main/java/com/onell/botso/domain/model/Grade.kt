package com.onell.botso.domain.model

/**
 * Modelo de dominio de una calificación.
 *
 * `score` está en escala 0.0-5.0, `weight` es el porcentaje (0.15 o 0.20) y
 * `termId` identifica el corte (1, 2 o 3).
 */
data class Grade(
    val id: String,
    val courseId: String,
    val name: String,
    val score: Double,
    val weight: Double,
    val termId: Int
)