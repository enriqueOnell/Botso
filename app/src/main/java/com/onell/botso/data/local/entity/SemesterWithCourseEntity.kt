package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relación Room `Semester -> Courses -> Sessions`.
 *
 * Se usa con `@Transaction` para traer un semestre completo con sus cursos y
 * horarios en una sola consulta.
 */
data class SemesterWithCourseEntity(
    @Embedded val semester: SemesterEntity,
    @Relation(
        entity = CourseEntity::class,
        parentColumn = "id",
        entityColumn = "semester_id"
    )
    val courses: List<CourseWithSessionEntity>
)
