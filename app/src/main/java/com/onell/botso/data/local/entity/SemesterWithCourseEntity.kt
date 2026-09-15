package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SemesterWithCourseEntity(
    @Embedded val semester: SemesterEntity,
    @Relation(
        entity = CourseEntity::class,
        parentColumn = "id",
        entityColumn = "semester_id"
    )
    val courses: List<CourseWithSessionEntity>
)
