package com.onell.botso.domain.model

import java.time.LocalDate

/**
 * Modelo de dominio de un semestre.
 *
 * Independiente de Room: usa `LocalDate` y no conoce anotaciones de persistencia.
 */
data class Semester(
    val id: String,
    val name: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val isActive: Boolean = true
)
