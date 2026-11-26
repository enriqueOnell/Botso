package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.components.dashboard.CourseGradeSectionCard
import com.onell.botso.ui.components.dashboard.PendingTasksCard
import com.onell.botso.ui.components.dashboard.PlatformSectionCard
import com.onell.botso.ui.components.dashboard.PriorityTasksSectionCard
import com.onell.botso.ui.components.dashboard.TodayClassSectionCard
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.DashboardUiState
import com.onell.botso.ui.viewmodel.DashboardViewModel
import java.time.LocalDate
import java.time.LocalTime

/**
 * Pantalla principal (Dashboard).
 *
 * Muestra el carrusel de plataformas académicas (EVA/SINU), las clases de hoy,
 * el contador de tareas pendientes, las tareas prioritarias y el promedio de
 * cada materia. Solo consume `DashboardUiState`; no contiene lógica de negocio.
 */
@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        modifier = modifier,
        uiState = uiState
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is DashboardUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is DashboardUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is DashboardUiState.Success -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(contentType = "Today Classes Section") {
                    TodayClassSectionCard(uiState.todayClasses)
                }
                
                item(contentType = "Pending Tasks Section") {
                    PendingTasksCard(count = uiState.pendingTasksCount)
                }

                item(contentType = "Platform Cards Section") {
                    PlatformSectionCard()
                }

                item(contentType = "Priority Tasks Section") {
                    PriorityTasksSectionCard(uiState.priorityTasks)
                }


                item(contentType = "Course Grade Section") {
                    CourseGradeSectionCard(uiState.coursesWithGrades)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    val tasks = listOf(
        Task(
            id = "id",
            courseId = "courseId",
            title = "Tarea 1",
            dueDate = LocalDate.now(),
            isPriority = true,
            hasAttachment = true,
            status = TaskStatus.TODO,
            week = 3,
            description = "Hay que hacer tarea 1"
        )
    )
    val courses = listOf(
        CourseWithGrades(
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            ),
            grades = listOf(
                Grade(
                    id = "g1",
                    courseId = "id2",
                    name = "Formativa",
                    score = 4.0,
                    weight = 0.5,
                    termId = 1
                )
            )
        ),
        CourseWithGrades(
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            ),
            grades = listOf(
                Grade(
                    id = "g1",
                    courseId = "id2",
                    name = "Formativa",
                    score = 4.0,
                    weight = 0.5,
                    termId = 1
                )
            )
        )
    )
    val todayClasses = listOf(
        CourseWithSession(
            session = ClassSession(
                "12",
                "id2",
                1,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "room",
                true
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            )
        ),
        CourseWithSession(
            session = ClassSession(
                "13",
                "24",
                1,
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                "room",
                true
            ),
            course = Course(
                id = "24",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            )
        )
    )

    BotsoTheme{
        DashboardContent(
            uiState = DashboardUiState.Success(
                priorityTasks = tasks,
                coursesWithGrades = courses,
                todayClasses = todayClasses
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview2() {
    BotsoTheme {
        DashboardContent(
            uiState = DashboardUiState.Success(
                priorityTasks = emptyList(),
                coursesWithGrades = emptyList(),
                todayClasses = emptyList()
            )
        )
    }
}
