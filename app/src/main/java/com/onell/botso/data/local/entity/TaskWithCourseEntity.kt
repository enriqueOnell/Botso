package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relación Room `Task -> Course` (desde la tarea).
 *
 * Variante de lectura usada cuando se parte de una tarea y se necesita su curso.
 */
data class TaskWithCourseEntity(
    @Embedded

    val task: TaskEntity,
    @Relation(
        entityColumn = "id",
        parentColumn = "course_id"
    )
    val course: CourseEntity
)
