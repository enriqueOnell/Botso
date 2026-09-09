package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "grades",
    foreignKeys = [
        ForeignKey(
            entity = CourseEntity::class,
            parentColumns = ["id"],
            childColumns = ["course_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["course_id"])]
)
data class GradeEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "score") val score: Double,
    @ColumnInfo(name = "weight") val weight: Double,
    @ColumnInfo(name = "termId") val termId: Int
)
