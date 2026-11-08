package com.onell.botso.navigation

import kotlinx.serialization.Serializable

/**
 * Rutas de navegación type-safe de Navigation Compose.
 *
 * Cada destino es `@Serializable`, lo que permite navegar con objetos tipados
 * (`composable<Route.Tasks>`) y leer argumentos con `toRoute()` sin construir
 * strings manualmente. `CourseGrades` transporta el `courseId` del curso.
 */
sealed interface Route {
    @Serializable
    data object Dashboard : Route


    @Serializable
    data object Tasks : Route

    @Serializable
    data object Semesters : Route

    @Serializable
    data class CourseGrades(val courseId: String) : Route

    @Serializable
    data object Settings : Route
}
