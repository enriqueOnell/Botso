package com.onell.botso.domain.model

data class SemesterWithCourse(
    val semester: Semester,
    val courses: List<Course>
)