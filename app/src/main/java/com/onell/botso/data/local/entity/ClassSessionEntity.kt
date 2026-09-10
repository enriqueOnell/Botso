package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

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
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "course_id")
    val courseId: String,
    @ColumnInfo(name = "day_of_week")
    val dayOfWeek: Int,
    @ColumnInfo(name = "start_time")
    val startTime: String,
    @ColumnInfo(name = "end_time")
    val endTime: String,
    @ColumnInfo(name = "room")
    val room: String = "",
    @ColumnInfo(name = "is_remote")
    val isRemote: Boolean = false
)