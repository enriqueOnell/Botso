package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class TaskWithCourseEntity(
    @Embedded

    val task: TaskEntity,
    @Relation(
        entityColumn = "id",
        parentColumn = "course_id"
    )
    val course: CourseEntity
)
