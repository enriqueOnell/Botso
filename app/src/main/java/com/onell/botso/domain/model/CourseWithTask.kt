package com.onell.botso.domain.model

/**
 * Par curso + tarea.
 *
 * Permite mostrar en cada tarjeta de tarea el nombre de la materia asociada.
 */
data class CourseWithTask(
    val course: Course,
    val task: Task
)
