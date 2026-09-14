package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import java.time.LocalDate

data class TasksUiState(
    val tasks: List<TaskWithCourse> = emptyList(),
    val courses: List<Course> = emptyList(),
    val columns: List<TasksColumnInfo> = listOf(
        TasksColumnInfo("TODO", "Por Hacer"),
        TasksColumnInfo("IN_PROGRESS", "En Progreso"),
        TasksColumnInfo("DONE", "Hecho")
    )
)

data class TasksColumnInfo(
    val status: String,
    val title: String
)

sealed class TasksUiEvent {
    data class OnAddTask(
        val courseId: String,
        val title: String,
        val dueDate: LocalDate,
        val isPriority: Boolean,
        val week: Int,
        val description: String
    ) : TasksUiEvent()

    data class OnUpdateTaskStatus(val task: Task) : TasksUiEvent()
    data class OnDeleteTask(val task: Task) : TasksUiEvent()
    data class OnUpdateTask(
        val task: Task,
        val courseId: String,
        val title: String,
        val dueDate: LocalDate,
        val isPriority: Boolean,
        val week: Int,
        val description: String
    ) : TasksUiEvent()
}
