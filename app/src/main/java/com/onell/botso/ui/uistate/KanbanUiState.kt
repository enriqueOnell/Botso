package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import java.time.LocalDate

data class KanbanUiState(
    val tasks: List<TaskWithCourse> = emptyList(),
    val courses: List<Course> = emptyList(),
    val columns: List<KanbanColumnInfo> = listOf(
        KanbanColumnInfo("TODO", "Por Hacer"),
        KanbanColumnInfo("IN_PROGRESS", "En Progreso"),
        KanbanColumnInfo("DONE", "Hecho")
    )
)

data class KanbanColumnInfo(
    val status: String,
    val title: String
)

sealed class KanbanUiEvent {
    data class OnAddTask(
        val courseId: String,
        val title: String,
        val dueDate: LocalDate,
        val isPriority: Boolean,
        val week: Int,
        val description: String
    ) : KanbanUiEvent()

    data class OnUpdateTaskStatus(val task: Task) : KanbanUiEvent()
    data class OnDeleteTask(val task: Task) : KanbanUiEvent()
    data class OnUpdateTask(
        val task: Task,
        val courseId: String,
        val title: String,
        val dueDate: LocalDate,
        val isPriority: Boolean,
        val week: Int,
        val description: String
    ) : KanbanUiEvent()
}
