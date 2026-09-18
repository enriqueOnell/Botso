package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithTask

sealed interface TasksUiState {
    data object Loading : TasksUiState

    data class Error(val message: String) : TasksUiState

    data class Success(
        val tasksWithCourses: List<CourseWithTask> = emptyList(),
        val courses: List<Course> = emptyList()
    ) : TasksUiState
}