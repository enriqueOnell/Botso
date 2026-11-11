package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
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
import com.onell.botso.ui.components.dashboard.ClassCard
import com.onell.botso.ui.components.dashboard.CourseGradeItem
import com.onell.botso.ui.components.dashboard.PendingTasksCard
import com.onell.botso.ui.components.dashboard.PlatformCard
import com.onell.botso.ui.components.dashboard.PriorityItem
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

    val eva = "https://evapresencial.americana.edu.co"
    val sinu = "https://campus.americana.edu.co/cua/sgacampus/#notr62"

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
                item {
                    PendingTasksCard(
                        count = uiState.pendingTasksCount,
                    )
                }

                item {
                    Text(
                        text = "Clases de Hoy",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                if (uiState.todayClasses.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "No hay clases programadas para hoy",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(
                        items = uiState.todayClasses,
                        key = { "Today ${it.session.id}" }
                    ) { courseWithSession ->
                        ClassCard(
                            sessionWithCourse = courseWithSession,
                        )
                    }
                }

                item {
                    Text(
                        text = "Plataformas",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }
                item {
                    HorizontalUncontainedCarousel(
                        state = rememberCarouselState { 2 }, // Le decimos que son exactamente 2 elementos
                        itemWidth = 300                                              .dp,
                        itemSpacing = 12.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) { index ->
                        when (index) {
                            0 -> {
                                PlatformCard(
                                    text = "EVA",
                                    url = eva,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .maskClip(MaterialTheme.shapes.extraLarge)
                                )
                            }

                            1 -> {
                                PlatformCard(
                                    text = "SINU",
                                    url = sinu,
                                    modifier = Modifier
                                        .fillMaxHeight()
                                        .maskClip(MaterialTheme.shapes.extraLarge)
                                )
                            }
                        }
                    }
                }

                item {
                    Text(
                        text = "Tareas Prioritarias",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                if (uiState.priorityTasks.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "No hay tareas prioritarias",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    val coursesMap =
                        uiState.coursesWithGrades.associate { it.course.id to it.course.name }
                    items(
                        items = uiState.priorityTasks,
                        key = { it.id }
                    ) { task ->
                        PriorityItem(
                            task = task,
                            courseName = coursesMap[task.courseId] ?: "",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                item {
                    Text(
                        text = "Mis Materias",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall,
                    )
                }

                if (uiState.coursesWithGrades.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(
                                text = "No hay materias registradas",
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                } else {
                    items(uiState.coursesWithGrades.chunked(2)) { rowCourses ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (courseWithGrades in rowCourses) {
                                CourseGradeItem(
                                    courseWithGrades = courseWithGrades,
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                )
                            }
                        }
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val tasks = listOf(
        Task(
            id = "id",
            courseId = "courseId",
            title = "title",
            dueDate = LocalDate.now(),
            isPriority = true,
            hasAttachment = true,
            status = TaskStatus.TODO,
            week = 3,
            description = ""
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
                LocalTime.of(10, 9),
                LocalTime.of(20, 17),
                "room",
                true
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            )
        )
    )

    DashboardContent(
        uiState = DashboardUiState.Success(
            priorityTasks = tasks,
            coursesWithGrades = courses,
            todayClasses = todayClasses
        )
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview2() {
    DashboardContent(
        uiState = DashboardUiState.Success(
            priorityTasks = emptyList(),
            coursesWithGrades = emptyList(),
            todayClasses = emptyList()
        )
    )
}
