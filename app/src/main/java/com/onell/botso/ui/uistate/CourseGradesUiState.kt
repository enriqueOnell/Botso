package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.CourseWithGrades

/**
 * Estados posibles de la pantalla de Notas.
 *
 * `Success` mantiene el corte actual, los textos en edición y el promedio del
 * corte, además de los datos persistidos del curso.
 */
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