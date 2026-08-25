package com.onell.UniApp.domain.model

import com.onell.UniApp.data.local.entity.ClassSession
import com.onell.UniApp.data.local.entity.Course

data class ClassSessionWithCourse(
    val session: ClassSession,
    val course: Course
)
