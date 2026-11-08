package com.onell.botso.domain.model

import java.time.LocalDate

/**
 * Modelo de dominio de una tarea académica.
 *
 * Incluye fecha límite (`LocalDate`), prioridad, semana, estado y descripción.
 */
data class Task(
    val id: String,
    val courseId: String,
    val title: String,
    val dueDate: LocalDate,
    val isPriority: Boolean = false,
    val hasAttachment: Boolean = false,
    val status: TaskStatus,
    val week: Int = 1,
    val description: String = ""
)
