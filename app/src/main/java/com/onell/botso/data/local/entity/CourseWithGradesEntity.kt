package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CourseWithGradesEntity(
    @Embedded
    val course: CourseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val grades: List<GradeEntity>
)
