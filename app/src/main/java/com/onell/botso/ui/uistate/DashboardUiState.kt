package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Task

typealias CourseWithAverage = CourseWithGrades

sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Error(val message: String) : DashboardUiState

    data class Success(
        val todayClasses: List<ClassSessionWithCourse> = emptyList(),
        val pendingTasksCount: Int = 0,
        val priorityTasks: List<Task> = emptyList(),
        val coursesWithGrades: List<CourseWithGrades> = emptyList()
    ) : DashboardUiState
}
