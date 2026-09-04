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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getSessionsForDayUseCase: GetSessionsForDayUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase, // 1. Cambiamos el arma aquí
    private val getAllCoursesUseCase: GetAllCoursesUseCase,
    private val getAllGradesUseCase: GetAllGradesUseCase
) : ViewModel() {

    private val dayOfWeek = LocalDate.now().dayOfWeek.value

    val uiState: StateFlow<DashboardUiState> = combine(
        getSessionsForDayUseCase(dayOfWeek),
        getAllTasksUseCase(), // 2. Disparamos la búsqueda global sin ID
        getAllCoursesUseCase(),
        getAllGradesUseCase()
    ) { sessions, allTasks, courses, grades ->
        val today = LocalDate.now()

        // 3. Forjamos el TaskWithCourse cruzando las dos listas manualmente
        val tasksWithCourse = allTasks.map { task ->
            // Buscamos a qué materia pertenece esta tarea
            val courseForTask = courses.firstOrNull { it.id == task.courseId }
                ?: return@map null // Si la materia no existe (no debería pasar), la descartamos

            TaskWithCourse(task = task, course = courseForTask)
        }.filterNotNull() // Limpiamos los nulos por seguridad

        val todayClasses = sessions.sortedBy { it.session.startTime }

        // Ahora puedes seguir usando tasksWithCourse exactamente igual que antes
        val pendingTasks = tasksWithCourse.filter { it.task.status != "DONE" }

        val priorities = pendingTasks.filter {
            it.task.isPriority ||
                    Instant.ofEpochMilli(it.task.dueDate).atZone(ZoneId.systemDefault())
                        .toLocalDate().isBefore(today.plusDays(1))
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

    // ... (calculateCourseAverage queda igual)
}

// El cálculo ahora exige la clase Grade del dominio
private fun calculateCourseAverage(grades: List<Grade>): Double {
    if (grades.isEmpty()) return 0.0
    val gradesByTerm = grades.groupBy { it.termId }
    val termAverages = gradesByTerm.map { (_, termGrades) ->
        termGrades.sumOf { it.score * it.weight }
    }
    return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
}
