package com.onell.botso.domain.model

import java.time.LocalTime

/**
 * Modelo de dominio de un horario de clase.
 *
 * `dayOfWeek` sigue el estándar `java.time.DayOfWeek` (1 = lunes, 7 = domingo).
 */
data class ClassSession(
    val id: String,
    val courseId: String,
    val dayOfWeek: Int,
    val startTime: LocalTime,
    val endTime: LocalTime,
    val room: String = "",
    val isRemote: Boolean = false
)
