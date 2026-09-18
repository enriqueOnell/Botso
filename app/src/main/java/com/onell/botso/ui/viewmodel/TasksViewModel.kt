package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.usecase.course.GetAllCoursesUseCase
import com.onell.botso.domain.usecase.task.DeleteTaskUseCase
import com.onell.botso.domain.usecase.task.GetAllTasksWithCourseUseCase
import com.onell.botso.domain.usecase.task.InsertTaskUseCase
import com.onell.botso.domain.usecase.task.UpdateTaskUseCase
import com.onell.botso.ui.uistate.TasksUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getAllTasksWithCourseUseCase: GetAllTasksWithCourseUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val insertTaskUseCase: InsertTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<TasksUiState>(TasksUiState.Loading)
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()

    init {
        loadAllTasks()
    }

    private fun loadAllTasks() {
        viewModelScope.launch {
            try {
                combine(
                    getAllTasksWithCourseUseCase(),
                    getAllCoursesUseCase()
                ){ tasks, courses ->
                    TasksUiState.Success(
                        tasksWithCourses = tasks,
                        courses = courses
                    )
                }
                .collect { updateState -> _uiState.value = updateState }
            } catch (e: Exception){
                _uiState.value = TasksUiState.Error("Error al cargar las tareas: ${e.message}")
            }
        }
    }

    fun insertTask(task: Task) {
        viewModelScope.launch {
            insertTaskUseCase(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
        }
    }

}
