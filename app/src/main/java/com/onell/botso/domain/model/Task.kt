package com.onell.botso.domain.model

data class Task(
val id: String,
val courseId: String,
val title: String,
val dueDate: Long,
val isPriority: Boolean,
val hasAttachment: Boolean = false,
val status: String,
val week: Int = 1,
val description: String = ""
)
