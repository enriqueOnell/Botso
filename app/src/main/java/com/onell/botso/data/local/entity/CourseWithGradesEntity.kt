package com.onell.botso.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Relación Room `Course -> Grades`.
 *
 * Modelo de lectura que agrupa un curso con todas sus calificaciones; alimenta el
 * promedio mostrado en Dashboard y en la pantalla de notas.
 */
data class CourseWithGradesEntity(
    @Embedded
    val course: CourseEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "course_id"
    )
    val grades: List<GradeEntity>
)
