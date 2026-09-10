package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats

data class SemestersUiState(
    val semestersWithStats: List<SemesterWithStats> = emptyList()
)

sealed class SemestersUiEvent {
    data class OnAddSemester(
        val id: String,
        val name: String,
        val startDate: Long,
        val endDate: Long,
        val isActive: Boolean
    ) : SemestersUiEvent()

    data class OnUpdateSemester(
        val semester: Semester,
        val name: String,
        val startDate: Long,
        val endDate: Long,
        val isActive: Boolean
    ) : SemestersUiEvent()

    data class OnDeleteSemester(val semester: Semester) : SemestersUiEvent()

    data class OnAddCourse(
        val semesterId: String,
        val course: Course,
        val session: ClassSession
    ) : SemestersUiEvent()

    data class OnEditCourse(
        val course: Course,
        val session: ClassSession
    ) : SemestersUiEvent()

    data class OnDeleteCourseConfirm(val course: Course) : SemestersUiEvent()
}