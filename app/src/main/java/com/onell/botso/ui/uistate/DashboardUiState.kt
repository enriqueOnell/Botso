package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.TaskWithCourse

data class DashboardUiState(
    val todayClasses: List<ClassSessionWithCourse> = emptyList(),
    val pendingTasksCount: Int = 0,
    val priorities: List<TaskWithCourse> = emptyList(),
    val coursesWithGrades: List<CourseWithAverage> = emptyList()
)

data class CourseWithAverage(
    val course: Course, // ¡Entidad erradicada! Usamos el modelo puro de dominio.
    val average: Double
)