package com.onell.UniApp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.UniApp.data.local.entity.Course
import com.onell.UniApp.data.local.entity.Task
import com.onell.UniApp.domain.model.TaskWithCourse
import com.onell.UniApp.domain.repository.UniRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class KanbanUiEvent {
    data class OnAddTask(val courseId: Long, val title: String, val dueDate: Long, val isPriority: Boolean, val week: Int, val description: String) : KanbanUiEvent()
    data class OnUpdateTaskStatus(val task: Task) : KanbanUiEvent()
    data class OnDeleteTask(val task: Task) : KanbanUiEvent()
    data class OnUpdateTask(val task: Task, val courseId: Long, val title: String, val dueDate: Long, val isPriority: Boolean, val week: Int, val description: String) : KanbanUiEvent()
}

@HiltViewModel
class KanbanViewModel @Inject constructor(
    private val repository: UniRepository
) : ViewModel() {

    val tasks: StateFlow<List<TaskWithCourse>> = repository.getTasksWithCourse()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val courses: StateFlow<List<Course>> = repository.getAllCourses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val columns: List<KanbanColumnInfo> = listOf(
        KanbanColumnInfo("TODO", "Por Hacer"),
        KanbanColumnInfo("IN_PROGRESS", "En Progreso"),
        KanbanColumnInfo("DONE", "Hecho")
    )

    fun onEvent(event: KanbanUiEvent) {
        when (event) {
            is KanbanUiEvent.OnAddTask -> addTask(event.courseId, event.title, event.dueDate, event.isPriority, event.week, event.description)
            is KanbanUiEvent.OnUpdateTaskStatus -> updateTaskStatus(event.task)
            is KanbanUiEvent.OnDeleteTask -> deleteTask(event.task)
            is KanbanUiEvent.OnUpdateTask -> updateTask(event.task, event.courseId, event.title, event.dueDate, event.isPriority, event.week, event.description)
        }
    }

    private fun addTask(courseId: Long, title: String, dueDate: Long, isPriority: Boolean, week: Int, description: String) {
        viewModelScope.launch {
            repository.insertTask(
                Task(
                    courseId = courseId,
                    title = title,
                    dueDate = dueDate,
                    isPriority = isPriority,
                    status = "TODO",
                    week = week,
                    description = description
                )
            )
        }
    }

    private fun updateTask(task: Task, courseId: Long, title: String, dueDate: Long, isPriority: Boolean, week: Int, description: String) {
        viewModelScope.launch {
            repository.updateTask(
                task.copy(
                    courseId = courseId,
                    title = title,
                    dueDate = dueDate,
                    isPriority = isPriority,
                    week = week,
                    description = description
                )
            )
        }
    }

    private fun updateTaskStatus(task: Task) {
        val nextStatus = when (task.status) {
            "TODO" -> "IN_PROGRESS"
            "IN_PROGRESS" -> "DONE"
            "DONE" -> "TODO"
            else -> "TODO"
        }
        viewModelScope.launch {
            repository.updateTask(task.copy(status = nextStatus))
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
}

data class KanbanColumnInfo(
    val status: String,
    val title: String
)
