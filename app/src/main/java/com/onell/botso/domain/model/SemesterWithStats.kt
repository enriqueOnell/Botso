package com.onell.botso.domain.model



data class SemesterWithStats(
    val semester: Semester,
    val courses: List<ClassSessionWithCourse>,
    val estimatedGpa: Double
) {
    val courseCount: Int get() = courses.size
}
