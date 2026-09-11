package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.usecase.course.GetCoursesWithGradesUseCase
import com.onell.botso.domain.usecase.session.GetSessionsForDayUseCase
import com.onell.botso.domain.usecase.task.GetAllPriorityTasksUseCase
import com.onell.botso.domain.usecase.task.GetAllTasksUseCase
import com.onell.botso.ui.uistate.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSessionsForDayUseCase: GetSessionsForDayUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllPriorityTasksUseCase: GetAllPriorityTasksUseCase,
    private val getCoursesWithGradesUseCase: GetCoursesWithGradesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val dayOfWeek: Int
        get() = LocalDate.now().dayOfWeek.value

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                combine(
                    getSessionsForDayUseCase(dayOfWeek),
                    getAllTasksUseCase(),
                    getAllPriorityTasksUseCase(),
                    getCoursesWithGradesUseCase()
                ) { sessions, tasks, priorityTasks, coursesWithGrades ->

                    DashboardUiState.Success(
                        todayClasses = sessions.sortedBy { it.session.startTime },
                        pendingTasksCount = tasks.count { it.status != "DONE" },
                        priorityTasks = priorityTasks,
                        coursesWithGrades = coursesWithGrades
                    )
                }
                    .collect { updatedState -> _uiState.value = updatedState }
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error("Error al cargar los datos: ${e.message}")
            }
        }
    }
}
