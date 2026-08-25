package com.onell.UniApp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.UniApp.data.local.entity.Course
import com.onell.UniApp.domain.model.ClassSessionWithCourse
import com.onell.UniApp.domain.model.TaskWithCourse
import com.onell.UniApp.domain.repository.UniRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: UniRepository
) : ViewModel() {

    private val dayOfWeek = LocalDate.now().dayOfWeek.value

    val uiState: StateFlow<DashboardUiState> = combine(
        repository.getSessionsForDay(dayOfWeek),
        repository.getTasksWithCourse(),
        repository.getAllCourses(),
        repository.getAllGrades()
    ) { sessions, tasksWithCourse, courses, grades ->
        val today = LocalDate.now()

        // Today's classes: all sessions for the current day
        val todayClasses = sessions.sortedBy { it.session.startTime }

        val pendingTasks = tasksWithCourse.filter { it.task.status != "DONE" }

        // Prioridades: Marked as priority OR due today/overdue
        val priorities = pendingTasks.filter {
            it.task.isPriority ||
            Instant.ofEpochMilli(it.task.dueDate).atZone(ZoneId.systemDefault()).toLocalDate().isBefore(today.plusDays(1))
        }.sortedBy { it.task.dueDate }

        val coursesWithGrades = courses.map { course ->
            val courseGrades = grades.filter { it.courseId == course.id }
            CourseWithAverage(course, calculateCourseAverage(courseGrades))
        }

        DashboardUiState(
            todayClasses = todayClasses,
            pendingTasksCount = pendingTasks.size,
            priorities = priorities,
            coursesWithGrades = coursesWithGrades
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DashboardUiState())

    private fun calculateCourseAverage(grades: List<com.onell.UniApp.data.local.entity.Grade>): Double {
        if (grades.isEmpty()) return 0.0
        val gradesByTerm = grades.groupBy { it.termId }
        val termAverages = gradesByTerm.map { (_, termGrades) ->
            termGrades.sumOf { it.score * it.weight }
        }
        return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
    }
}

data class DashboardUiState(
    val todayClasses: List<ClassSessionWithCourse> = emptyList(),
    val pendingTasksCount: Int = 0,
    val priorities: List<TaskWithCourse> = emptyList(),
    val coursesWithGrades: List<CourseWithAverage> = emptyList()
)

data class CourseWithAverage(
    val course: Course,
    val average: Double
)
