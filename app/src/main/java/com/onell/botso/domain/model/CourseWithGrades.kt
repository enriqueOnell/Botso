package com.onell.botso.domain.model

data class CourseWithGrades(
    val course: Course,
    val grades: List<Grade>
) {
    val averageGrade: Double
        get() {
            if (grades.isEmpty()) return 0.0
            val gradesByTerm = grades.groupBy { it.termId }
            val termAverages = gradesByTerm.map { (_, termGrades) ->
                termGrades.sumOf { it.score * it.weight }
            }
            return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
        }
}
