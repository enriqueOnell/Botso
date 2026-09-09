package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "class_sessions",
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
data class ClassSessionEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "course_id") val courseId: String,
    @ColumnInfo(name = "day_of_week") val dayOfWeek: Int, // 1 (Mon) to 7 (Sun)
    @ColumnInfo(name = "start_time") val startTime: String, // HH:mm
    @ColumnInfo(name = "end_time") val endTime: String, // HH:mm
    @ColumnInfo(name = "room") val room: String = ""
)
