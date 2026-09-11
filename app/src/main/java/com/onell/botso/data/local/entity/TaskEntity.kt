package com.onell.botso.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
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
data class TaskEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "course_id")
    val courseId: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "due_date")
    val dueDate: String,
    @ColumnInfo(name = "is_priority")
    val isPriority: Boolean = false,
    @ColumnInfo(name = "has_attachment")
    val hasAttachment: Boolean = false,
    @ColumnInfo(name = "status")
    val status: String = "TODO",
    @ColumnInfo(name = "week")
    val week: Int = 1,
    @ColumnInfo(name = "description")
    val description: String = ""
)
