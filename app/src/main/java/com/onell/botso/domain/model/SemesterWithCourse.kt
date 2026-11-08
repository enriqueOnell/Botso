package com.onell.botso.domain.model

/**
 * Semestre con todos sus cursos y horarios.
 *
 * Es el modelo que consume la pantalla de Semestres.
 */
data class SemesterWithCourse(
    val semester: Semester,
    val courses: List<CourseWithSession>
)
