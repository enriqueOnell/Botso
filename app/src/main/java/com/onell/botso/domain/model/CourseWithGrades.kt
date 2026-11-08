package com.onell.botso.domain.model

/**
 * Curso junto con sus calificaciones.
 *
 * `averageGrade` calcula el promedio ponderado sumando `score * weight` de cada
 * nota. Es la única fuente del promedio mostrado en la UI.
 */
data class CourseWithGrades(
    val course: Course,
    val grades: List<Grade>
) {
    val averageGrade: Double
        get() {
            if (grades.isEmpty()) return 0.0
            return grades.sumOf { it.score * it.weight }
        }
}
