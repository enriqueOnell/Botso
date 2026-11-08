package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relación Room `Course -> ClassSessions`.
 *
 * Agrupa un curso con sus horarios. Es la base para construir la vista de
 * semestres y las clases del día.
 */
data class CourseWithSessionEntity(
    @Embedded
    val course: CourseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val sessions: List<ClassSessionEntity>
)
