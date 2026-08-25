package com.onell.UniApp.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = Course::class,
            parentColumns = ["id"],
            childColumns = ["courseId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["courseId"])]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val courseId: Long,
    val title: String,
    val dueDate: Long,
    val isPriority: Boolean,
    val hasAttachment: Boolean = false,
    val status: String, // e.g., "TODO", "IN_PROGRESS", "DONE"
    val week: Int = 1,
    val description: String = ""
)
