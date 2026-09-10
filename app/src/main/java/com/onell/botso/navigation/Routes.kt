package com.onell.botso.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object Dashboard : Route

    @Serializable
    data object Kanban : Route

    @Serializable
    data object Semesters : Route

    @Serializable
    data class CourseGrades(val courseId: String) : Route
}
