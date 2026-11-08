package com.onell.botso.domain.model

/**
 * Par curso + sesión de horario.
 *
 * Se usa para pintar tarjetas de clase y filas de materias sin consultar la BD
 * de nuevo.
 */
data class CourseWithSession(
    val course: Course,
    val session: ClassSession
)
