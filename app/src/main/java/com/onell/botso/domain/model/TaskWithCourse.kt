package com.onell.botso.domain.model

import com.onell.botso.data.local.entity.Task
import com.onell.botso.data.local.entity.Course

data class TaskWithCourse(
    val task: Task,
    val course: Course
)
