package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.Grade

data class CourseGradesUiState(
    val courseId: Long? = null,
    val currentTermId: Int = 1,
    val stagedFormativa: String = "",
    val stagedCognitiva: String = "",
    val grades: List<Grade> = emptyList(),
    val averageScore: Double = 0.0,
    val currentTermAverage: Double = 0.0
)

sealed class CourseGradesUiEvent {
    data class OnSetCourseId(val id: Long) : CourseGradesUiEvent()
    data class OnSetTermId(val termId: Int) : CourseGradesUiEvent()
    data class OnUpdateFormativa(val value: String) : CourseGradesUiEvent()
    data class OnUpdateCognitiva(val value: String) : CourseGradesUiEvent()
    object OnSave : CourseGradesUiEvent()
}