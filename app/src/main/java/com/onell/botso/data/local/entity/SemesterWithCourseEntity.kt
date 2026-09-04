package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SemesterWithCourseEntity(
    @Embedded val semester: SemesterEntity,

    @Relation(
        parentColumn = "id", // Revisa cómo se llama el ID en tu SemesterEntity
        entityColumn = "semester_id" // Revisa cómo se llama la llave foránea en CourseEntity
    )
    val courses: List<CourseEntity>
)