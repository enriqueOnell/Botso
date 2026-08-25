package com.onell.UniApp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.UniApp.data.local.entity.ClassSession
import com.onell.UniApp.data.local.entity.Course
import com.onell.UniApp.domain.repository.UniRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ScheduleUiEvent {
    data class OnAddSession(val courseId: Long, val dayOfWeek: Int, val startTime: String, val endTime: String, val room: String) : ScheduleUiEvent()
    data class OnDeleteSession(val session: ClassSession) : ScheduleUiEvent()
    data class OnUpdateSession(val session: ClassSession) : ScheduleUiEvent()
    data class OnUpdateCourse(val course: Course) : ScheduleUiEvent()
    data class OnDeleteCourse(val course: Course) : ScheduleUiEvent()
}

data class ScheduleEntry(
    val name: String,
    val dayOfWeek: Int,
    val startTime: String,
    val endTime: String,
    val location: String,
    val colorHex: String,
    val course: Course? = null,
    val session: ClassSession? = null
)

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    private val repository: UniRepository
) : ViewModel() {

    private val courses = repository.getAllCourses()
    private val allSessions = repository.getAllClassSessions()

    val scheduleEntries: StateFlow<List<ScheduleEntry>> = combine(allSessions, courses) { sessions, courses ->
        val sessionEntries = sessions.map {
            ScheduleEntry(
                name = it.course.name,
                dayOfWeek = it.session.dayOfWeek,
                startTime = it.session.startTime,
                endTime = it.session.endTime,
                location = it.session.room,
                colorHex = it.course.colorHex,
                course = it.course,
                session = it.session
            )
        }

        val coursesWithSessions = sessions.map { it.course.id }.toSet()
        val courseEntries = courses.filter { it.id !in coursesWithSessions && it.startTime.isNotBlank() }
            .map {
                ScheduleEntry(
                    name = it.name,
                    dayOfWeek = it.dayOfWeek,
                    startTime = it.startTime,
                    endTime = it.endTime,
                    location = it.location,
                    colorHex = it.colorHex,
                    course = it,
                    session = null
                )
            }

        sessionEntries + courseEntries
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onEvent(event: ScheduleUiEvent) {
        when (event) {
            is ScheduleUiEvent.OnAddSession -> addSession(event.courseId, event.dayOfWeek, event.startTime, event.endTime, event.room)
            is ScheduleUiEvent.OnDeleteSession -> deleteSession(event.session)
            is ScheduleUiEvent.OnUpdateSession -> updateSession(event.session)
            is ScheduleUiEvent.OnUpdateCourse -> updateCourse(event.course)
            is ScheduleUiEvent.OnDeleteCourse -> deleteCourse(event.course)
        }
    }

    private fun addSession(courseId: Long, dayOfWeek: Int, startTime: String, endTime: String, room: String) {
        viewModelScope.launch {
            repository.insertClassSession(
                ClassSession(
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
            repository.updateClassSession(session)
        }
    }

    private fun deleteSession(session: ClassSession) {
        viewModelScope.launch {
            repository.deleteClassSession(session)
        }
    }

    private fun updateCourse(course: Course) {
        viewModelScope.launch {
            repository.updateCourse(course)
        }
    }

    private fun deleteCourse(course: Course) {
        viewModelScope.launch {
            repository.deleteCourse(course)
        }
    }
}
