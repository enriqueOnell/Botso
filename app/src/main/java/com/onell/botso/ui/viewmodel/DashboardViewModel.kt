package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.usecase.course.GetAllCoursesUseCase
import com.onell.botso.domain.usecase.grade.GetAllGradesUseCase
import com.onell.botso.domain.usecase.session.GetSessionsForDayUseCase
import com.onell.botso.domain.usecase.task.GetAllTasksUseCase
import com.onell.botso.ui.uistate.CourseWithAverage
import com.onell.botso.ui.uistate.DashboardUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSessionsForDayUseCase: GetSessionsForDayUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val getAllGradesUseCase: GetAllGradesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

     init {
         loadDashboardData()
     }

    val dayOfWeek = LocalDate.now().dayOfWeek.value

    private fun loadDashboardData() {
        viewModelScope.launch {
            try {
                combine(
                    getSessionsForDayUseCase(dayOfWeek),
                    getAllTasksUseCase(),
                    getAllCoursesUseCase(),
                    getAllGradesUseCase()
                ) { sessions, tasks, courses, grades ->

                    // Cruce de Task con Course para generar TaskWithCourse
                    val priorityTasksWithCourse = tasks
                        .filter { it.isPriority && it.status != "DONE" }
                        .sortedBy { it.dueDate }
                        .mapNotNull { task ->
                            val course = courses.firstOrNull { it.id == task.courseId }
                            if (course != null) TaskWithCourse(task = task, course = course) else null
                        }

                    DashboardUiState.Success(
                        todayClasses = sessions.sortedBy { it.session.startTime },
                        pendingTasksCount = tasks.count { it.status != "DONE" },
                        priorityTasks = priorityTasksWithCourse,
                        coursesWithGrades = courses.map { course ->
                            val courseGrades = grades.filter { it.courseId == course.id }
                            CourseWithAverage(course, calculateCourseAverage(courseGrades))
                        }
                    )
                }
                    .collect { updatedState -> _uiState.value = updatedState }
            } catch (e: Exception) {
                _uiState.value = DashboardUiState.Error("Error al cargar los datos: ${e.message}")
            }
        }
    }
}

private fun calculateCourseAverage(grades: List<Grade>): Double {
    if (grades.isEmpty()) return 0.0
    val gradesByTerm = grades.groupBy { it.termId }
    val termAverages = gradesByTerm.map { (_, termGrades) ->
        termGrades.sumOf { it.score * it.weight }
    }
    return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
}
