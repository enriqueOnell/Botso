package com.onell.botso.domain.model

import androidx.room.PrimaryKey

data class Task(
val id: Long = 0,
val courseId: Long,
val title: String,
val dueDate: Long,
val isPriority: Boolean,
val hasAttachment: Boolean = false,
val status: String, // e.g., "TODO", "IN_PROGRESS", "DONE"
val week: Int = 1,
val description: String = ""
)
