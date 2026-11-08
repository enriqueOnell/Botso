package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relación Room `Course -> Tasks`.
 *
 * Permite listar las tareas junto con la materia a la que pertenecen.
 */
data class CourseWithTaskEntity(
    @Embedded
    val course: CourseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val tasks: List<TaskEntity>
)
