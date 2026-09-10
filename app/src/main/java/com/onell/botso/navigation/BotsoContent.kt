package com.onell.botso.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.runtime.NavEntry
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.onell.botso.ui.screens.*

@Composable
fun BotsoContent() {
    val backStack = remember { mutableStateListOf<Route>(Route.Dashboard) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val currentRoute = backStack.lastOrNull() ?: Route.Dashboard
            BotsoBottomNavigationBar(
                currentRoute = currentRoute,
                onNavigate = { route ->
                    if (backStack.lastOrNull() != route) {
                        if (route is Route.Dashboard || 
                            route is Route.Kanban || route is Route.Semesters) {
                            backStack.clear()
                        }
                        backStack.add(route)
                    }
                }
            )
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            modifier = Modifier.padding(innerPadding),
            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) },
            entryProvider = { key ->
                when (key) {
                    is Route.Dashboard -> NavEntry(key) { DashboardScreen() }
                    is Route.Kanban -> NavEntry(key) { KanbanScreen() }
                    is Route.Semesters -> NavEntry(key) { 
                        SemestersScreen(
                            onNavigateToCourseGrades = { courseId -> backStack.add(Route.CourseGrades(courseId)) }
                        ) 
                    }
                    is Route.CourseGrades -> NavEntry(key) { 
                        CourseGradesScreen(
                            courseId = key.courseId,
                            onBack = { if (backStack.size > 1) backStack.removeAt(backStack.size - 1) }
                        ) 
                    }
                    else -> NavEntry(key) { DashboardScreen() }
                }
            }
        )
    }
}
