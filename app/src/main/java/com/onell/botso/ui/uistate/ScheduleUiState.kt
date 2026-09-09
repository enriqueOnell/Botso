package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course

data class ScheduleUiState(
    val scheduleEntries: List<ScheduleEntry> = emptyList()
)

data class ScheduleEntry(
    val name: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val location: String,
    val colorHex: String,
    val course: Course? = null,      // Reemplazado por el modelo puro
    val session: ClassSession? = null // Reemplazado por el modelo puro
)

sealed class ScheduleUiEvent {

    data class OnAddSession(
        val id: String,
        val courseId: String,
        val dayOfWeek: Int,
        val startTime: String,
        val endTime: String,
        val room: String
    ) : ScheduleUiEvent()

    data class OnDeleteSession(val session: ClassSession) : ScheduleUiEvent()
    data class OnUpdateSession(val session: ClassSession) : ScheduleUiEvent()
    data class OnUpdateCourse(val course: Course) : ScheduleUiEvent()
    data class OnDeleteCourse(val course: Course) : ScheduleUiEvent()
}