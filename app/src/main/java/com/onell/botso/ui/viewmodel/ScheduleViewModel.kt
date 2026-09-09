package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.usecase.course.DeleteCourseUseCase
import com.onell.botso.domain.usecase.course.GetAllCoursesUseCase
import com.onell.botso.domain.usecase.course.UpdateCourseUseCase
import com.onell.botso.domain.usecase.session.DeleteClassSessionUseCase
import com.onell.botso.domain.usecase.session.GetAllClassSessionsUseCase
import com.onell.botso.domain.usecase.session.InsertClassSessionUseCase
import com.onell.botso.domain.usecase.session.UpdateClassSessionUseCase
import com.onell.botso.ui.uistate.ScheduleEntry
import com.onell.botso.ui.uistate.ScheduleUiEvent
import com.onell.botso.ui.uistate.ScheduleUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val getAllClassSessionsUseCase: GetAllClassSessionsUseCase,
    private val insertClassSessionUseCase: InsertClassSessionUseCase,
    private val updateClassSessionUseCase: UpdateClassSessionUseCase,
    private val deleteClassSessionUseCase: DeleteClassSessionUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase
) : ViewModel() {

    // Un único flujo maestro para gobernar el horario
    val uiState: StateFlow<ScheduleUiState> = combine(
        getAllClassSessionsUseCase(),
        getAllCoursesUseCase()
    ) { allSessions, courses ->

        // 1. Emparejamos las sesiones reales con sus respectivas materias
        val sessionEntries = allSessions.mapNotNull { session ->
            val courseForSession = courses.firstOrNull { it.id == session.courseId }
            if (courseForSession != null) {
                ScheduleEntry(
                    name = courseForSession.name,
                    dayOfWeek = session.dayOfWeek,
                    startTime = session.startTime,
                    endTime = session.endTime,
                    location = session.room,
                    colorHex = courseForSession.colorHex,
                    course = courseForSession,
                    session = session
                )
            } else null
        }

        // 2. Mantenemos tu lógica defensiva para mostrar "sesiones fantasma" de materias sin horario oficial
        val coursesWithSessions = allSessions.map { it.courseId }.toSet()
        val courseEntries =
            courses.filter { it.id !in coursesWithSessions && it.startTime.isNotBlank() }
                .map { course ->
                    ScheduleEntry(
                        name = course.name,
                        dayOfWeek = course.dayOfWeek,
                        startTime = course.startTime,
                        endTime = course.endTime,
                        location = course.location,
                        colorHex = course.colorHex,
                        course = course,
                        session = null
                    )
                }

        // 3. Empaquetamos todo en el nuevo estado puro
        ScheduleUiState(
            scheduleEntries = sessionEntries + courseEntries
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ScheduleUiState())

    fun onEvent(event: ScheduleUiEvent) {
        when (event) {
            is ScheduleUiEvent.OnAddSession -> addSession(
                event.id,
                event.courseId,
                event.dayOfWeek,
                event.startTime,
                event.endTime,
                event.room
            )

            is ScheduleUiEvent.OnDeleteSession -> deleteSession(event.session)
            is ScheduleUiEvent.OnUpdateSession -> updateSession(event.session)
            is ScheduleUiEvent.OnUpdateCourse -> updateCourse(event.course)
            is ScheduleUiEvent.OnDeleteCourse -> deleteCourse(event.course)
        }
    }

    private fun addSession(
        id: String,
        courseId: String,
        dayOfWeek: Int,
        startTime: String,
        endTime: String,
        room: String
    ) {
        viewModelScope.launch {
            insertClassSessionUseCase(
                // Construimos el modelo puro
                ClassSession(
                    id = id,
                    courseId = courseId,
                    dayOfWeek = dayOfWeek,
                    startTime = startTime,
                    endTime = endTime,
                    room = room
                )
            )
        }
    }

    private fun updateSession(session: ClassSession) {
        viewModelScope.launch {
            updateClassSessionUseCase(session)
        }
    }

    private fun deleteSession(session: ClassSession) {
        viewModelScope.launch {
            deleteClassSessionUseCase(session)
        }
    }

    private fun updateCourse(course: Course) {
        viewModelScope.launch {
            updateCourseUseCase(course)
        }
    }

    private fun deleteCourse(course: Course) {
        viewModelScope.launch {
            deleteCourseUseCase(course)
        }
    }
}