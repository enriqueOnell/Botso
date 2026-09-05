package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.ui.components.dashboard.ClassCard
import com.onell.botso.ui.components.dashboard.CourseGradeItem
import com.onell.botso.ui.components.dashboard.PendingTasksCard
import com.onell.botso.ui.components.dashboard.PriorityItem
import com.onell.botso.ui.uistate.CourseWithAverage
import com.onell.botso.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        todayClasses = uiState.todayClasses,
        pendingTasksCount = uiState.pendingTasksCount,
        priorities = uiState.priorities,
        courses = uiState.coursesWithGrades
    )
}

@Composable
fun DashboardContent(
    todayClasses: List<ClassSessionWithCourse>,
    pendingTasksCount: Int,
    priorities: List<TaskWithCourse>,
    courses: List<CourseWithAverage>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Resumen del Día",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        item {
            Text(
                text = "Clases de Hoy",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (todayClasses.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = "No hay clases programadas para hoy",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        } else {
            items(todayClasses) { sessionWithCourse ->
                ClassCard(sessionWithCourse)
            }
        }

        item {
            PendingTasksCard(pendingTasksCount)
        }

        item {
            Text(
                text = "Tareas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(priorities) { taskWithCourse ->
            PriorityItem(taskWithCourse)
        }

        item {
            Text(
                text = "Mis Materias",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(courses) { courseWithAverage ->
            CourseGradeItem(courseWithAverage)
        }
    }
}