package com.onell.botso.domain.model

import com.onell.botso.data.local.entity.ClassSession
import com.onell.botso.data.local.entity.Course

data class ClassSessionWithCourse(
    val session: ClassSession,
    val course: Course
)
