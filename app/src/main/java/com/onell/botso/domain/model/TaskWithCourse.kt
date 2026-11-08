package com.onell.botso.domain.model

/**
 * Par tarea + curso (variante de lectura).
 *
 * Se conserva para consultas que parten de la tarea.
 */
data class TaskWithCourse(
    val task: Task,
    val course: Course
)
