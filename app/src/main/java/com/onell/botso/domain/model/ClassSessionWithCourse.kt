package com.onell.botso.domain.model

/**
 * Par sesión + curso.
 *
 * Modelo auxiliar para representar un horario junto a su materia.
 */
data class ClassSessionWithCourse(
    val session: ClassSession,
    val course: Course
)
