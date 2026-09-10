package com.onell.botso.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ViewKanban
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun BotsoBottomNavigationBar(
    currentRoute: Route,
    onNavigate: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBar(modifier = modifier) {
        val items = listOf(
            BottomNavItem("Dashboard", Route.Dashboard, Icons.Default.Dashboard),
            BottomNavItem("Kanban", Route.Kanban, Icons.Default.ViewKanban),
            BottomNavItem("Semesters", Route.Semesters, Icons.AutoMirrored.Filled.List)
        )

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = { onNavigate(item.route) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}

private data class BottomNavItem(
    val label: String,
    val route: Route,
    val icon: ImageVector
)
