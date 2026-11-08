package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Task

/**
 * Estados posibles del Dashboard: cargando, error o datos listos para pintar.
 */
sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Success(
        val todayClasses: List<CourseWithSession> = emptyList(),
        val pendingTasksCount: Int = 0,
        val priorityTasks: List<Task> = emptyList(),
        val coursesWithGrades: List<CourseWithGrades> = emptyList()
    ) : DashboardUiState

    data class Error(val message: String) : DashboardUiState
}
