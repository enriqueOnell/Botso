package com.onell.botso.domain.model

data class Task(
    val id: String,
    val courseId: String,
    val title: String,
    val dueDate: Long,
    val isPriority: Boolean = false,
    val hasAttachment: Boolean = false,
    val status: String = "TODO",
    val week: Int = 1,
    val description: String = ""
)