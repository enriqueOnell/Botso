package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.usecase.course.GetAllCoursesUseCase
import com.onell.botso.domain.usecase.task.DeleteTaskUseCase
import com.onell.botso.domain.usecase.task.GetAllTasksUseCase
import com.onell.botso.domain.usecase.task.InsertTaskUseCase
import com.onell.botso.domain.usecase.task.UpdateTaskUseCase
import com.onell.botso.ui.uistate.TasksUiEvent
import com.onell.botso.ui.uistate.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    val uiState: StateFlow<TasksUiState> = combine(
        getAllTasksUseCase(),
        getAllCoursesUseCase()
    ) { allTasks, courses ->

        val tasksWithCourse = allTasks.mapNotNull { task ->
            val courseForTask = courses.firstOrNull { it.id == task.courseId }
            if (courseForTask != null) {
                CourseWithTask(task = task, course = courseForTask)
            } else null
        }

        TasksUiState(
            tasks = tasksWithCourse,
            courses = courses
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TasksUiState())

    fun onEvent(event: TasksUiEvent) {
        when (event) {
            is TasksUiEvent.OnAddTask -> addTask(
                event.courseId,
                event.title,
                event.dueDate,
                event.isPriority,
                event.week,
                event.description
            )

            is TasksUiEvent.OnUpdateTaskStatus -> updateTaskStatus(event.task)
            is TasksUiEvent.OnDeleteTask -> deleteTask(event.task)
            is TasksUiEvent.OnUpdateTask -> updateTask(
                event.task,
                event.courseId,
                event.title,
                event.dueDate,
                event.isPriority,
                event.week,
                event.description
            )
        }
    }

    private fun addTask(
        courseId: String,
        title: String,
        dueDate: LocalDate,
        isPriority: Boolean,
        week: Int,
        description: String
    ) {
        viewModelScope.launch {
            insertTaskUseCase(
                Task(
                    id = UUID.randomUUID().toString(),
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

    private fun updateTask(
        task: Task,
        courseId: String,
        title: String,
        dueDate: LocalDate,
        isPriority: Boolean,
        week: Int,
        description: String
    ) {
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
