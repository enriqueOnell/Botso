package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithTaskEntity(
    @Embedded
    val course: CourseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val tasks: List<TaskEntity>
)
