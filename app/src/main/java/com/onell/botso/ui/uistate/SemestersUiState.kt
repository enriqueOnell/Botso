package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats

data class SemestersUiState(
    val semestersWithStats: List<SemesterWithStats> = emptyList()
)

sealed class SemestersUiEvent {
    data class OnAddSemester(
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
        val semesterId: Long,
        val name: String,
        val dayOfWeek: Int,
        val startTime: String,
        val endTime: String,
        val professor: String,
        val colorHex: String,
        val location: String,
        val isRemote: Boolean
    ) : SemestersUiEvent()

    data class OnEditCourse(
        val course: Course,
        val name: String,
        val dayOfWeek: Int,
        val startTime: String,
        val endTime: String,
        val professor: String,
        val colorHex: String,
        val location: String,
        val isRemote: Boolean
    ) : SemestersUiEvent()

    data class OnDeleteCourseConfirm(val course: Course) : SemestersUiEvent()
}