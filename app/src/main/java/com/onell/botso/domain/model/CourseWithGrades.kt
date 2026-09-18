package com.onell.botso.domain.model

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
