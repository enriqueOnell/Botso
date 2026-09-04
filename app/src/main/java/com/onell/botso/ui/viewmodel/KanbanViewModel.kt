package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.usecase.course.GetAllCoursesUseCase
import com.onell.botso.domain.usecase.task.DeleteTaskUseCase
import com.onell.botso.domain.usecase.task.GetAllTasksUseCase
import com.onell.botso.domain.usecase.task.InsertTaskUseCase
import com.onell.botso.domain.usecase.task.UpdateTaskUseCase
import com.onell.botso.ui.uistate.KanbanUiEvent
import com.onell.botso.ui.uistate.KanbanUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KanbanViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    // Centralizamos todo en un único flujo de estado
    val uiState: StateFlow<KanbanUiState> = combine(
        getAllTasksUseCase(),
        getAllCoursesUseCase()
    ) { allTasks, courses ->

        // Cruzamos las listas manualmente para emparejar la tarea con su materia
        val tasksWithCourse = allTasks.mapNotNull { task ->
            val courseForTask = courses.firstOrNull { it.id == task.courseId }
            if (courseForTask != null) {
                TaskWithCourse(task = task, course = courseForTask)
            } else null
        }

        KanbanUiState(
            tasks = tasksWithCourse,
            courses = courses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), KanbanUiState())

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
            insertTaskUseCase(
                // Forjamos el modelo de Dominio (Task) en lugar de TaskEntity
                Task(
                    id = 0,
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
            updateTaskUseCase(
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
            updateTaskUseCase(task.copy(status = nextStatus))
        }
    }

    private fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }
}