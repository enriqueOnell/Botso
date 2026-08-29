package com.onell.botso.domain.model

import com.onell.botso.data.local.entity.Course
import com.onell.botso.data.local.entity.Semester

data class SemesterWithStats(
    val semester: Semester,
    val courses: List<Course>,
    val estimatedGpa: Double
) {
    val courseCount: Int get() = courses.size
}
