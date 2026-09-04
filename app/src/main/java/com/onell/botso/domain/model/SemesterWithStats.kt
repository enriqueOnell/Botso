package com.onell.botso.domain.model



data class SemesterWithStats(
    val semester: Semester,
    val courses: List<Course>,
    val estimatedGpa: Double
) {
    val courseCount: Int get() = courses.size
}
