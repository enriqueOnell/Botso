package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse

sealed interface SemestersUiState {
    data object Loading : SemestersUiState

    data class Success(
        val allSemester: List<Semester> = emptyList(),
        val semesterWithCourses: List<SemesterWithCourse> = emptyList(),
        val gradesForCourse: List<CourseWithGrades> = emptyList()
    ): SemestersUiState

    data class Error(val message: String) : SemestersUiState
}