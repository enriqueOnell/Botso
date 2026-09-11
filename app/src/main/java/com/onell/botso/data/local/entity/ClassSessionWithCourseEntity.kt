package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class ClassSessionWithCourseEntity(
    @Embedded
    val classSession: ClassSessionEntity,

    @Relation(
        parentColumn = "course_id",
        entityColumn = "id"
    )
    val course: CourseEntity
)
