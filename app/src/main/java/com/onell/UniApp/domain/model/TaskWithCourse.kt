package com.onell.UniApp.domain.model

import com.onell.UniApp.data.local.entity.Task
import com.onell.UniApp.data.local.entity.Course

data class TaskWithCourse(
    val task: Task,
    val course: Course
)
