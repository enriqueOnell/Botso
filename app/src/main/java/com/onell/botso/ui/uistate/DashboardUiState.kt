package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskWithCourse

sealed interface DashboardUiState {
    data object Loading : DashboardUiState

    data class Error(val message: String) : DashboardUiState

    data class Success(
        val todayClasses: List<ClassSessionWithCourse> = emptyList(),
        val pendingTasksCount: Int = 0,
        val priorityTasks: List<TaskWithCourse> = emptyList(),
        val coursesWithGrades: List<CourseWithAverage> = emptyList()
    ) : DashboardUiState
}

data class CourseWithAverage(
    val course: Course,
    val averageGrade: Double
)