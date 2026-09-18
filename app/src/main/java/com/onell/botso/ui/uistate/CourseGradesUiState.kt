package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.CourseWithGrades

sealed interface CourseGradesUiState {
    data object Loading : CourseGradesUiState

    data class Error(val message: String) : CourseGradesUiState

    data class Success(
        val courseData: CourseWithGrades? = null,
        val currentTermId: Int = 1,
        val stagedFormativa: String = "",
        val stagedCognitiva: String = "",
        val currentTermAverage: Double = 0.0
    ) : CourseGradesUiState
}