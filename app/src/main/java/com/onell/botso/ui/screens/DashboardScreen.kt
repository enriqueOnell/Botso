package com.onell.botso.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Class
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.data.local.entity.ClassSession
import com.onell.botso.data.local.entity.Course
import com.onell.botso.data.local.entity.Task
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.viewmodel.CourseWithAverage
import com.onell.botso.ui.viewmodel.DashboardViewModel
import java.util.Locale

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

@Composable
fun ClassCard(sessionWithCourse: ClassSessionWithCourse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${sessionWithCourse.session.startTime} - ${sessionWithCourse.session.endTime}",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                if (sessionWithCourse.course.isRemote) {
                    Surface(
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Remota",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiary
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = sessionWithCourse.course.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Class, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sessionWithCourse.course.location,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Rounded.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = sessionWithCourse.course.professor,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun CourseGradeItem(courseWithAverage: CourseWithAverage) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = courseWithAverage.course.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = courseWithAverage.course.code,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Surface(
                color = if (courseWithAverage.average >= 3.0) Color(0xFFA0F399) else Color(0xFFFFDAD6),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "%.2f", courseWithAverage.average),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (courseWithAverage.average >= 3.0) Color(0xFF002106) else Color(0xFF410002)
                )
            }
        }
    }
}

@Composable
fun PendingTasksCard(count: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tareas Pendientes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFA0F399)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF002106)
                )
            }
        }
    }
}

@Composable
fun PriorityItem(taskWithCourse: TaskWithCourse) {
    val indicatorColor = if (taskWithCourse.task.dueDate < System.currentTimeMillis() + 86400000) {
        Color(0xFFBA1A1A) // Red (Vence hoy)
    } else {
        Color(0xFFA0F399) // Green (Mañana)
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(40.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(indicatorColor)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = taskWithCourse.task.title,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(24.dp)
                    ) {
                        Text(
                            text = "Semana ${taskWithCourse.task.week}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                Text(
                    text = taskWithCourse.course.name,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (taskWithCourse.task.description.isNotBlank()) {
                    Text(
                        text = taskWithCourse.task.description,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardPreview() {
    UniAppTheme {
        DashboardContent(
            todayClasses = listOf(
                ClassSessionWithCourse(
                    session = ClassSession(courseId = 1, dayOfWeek = 1, startTime = "10:00", endTime = "11:30"),
                    course = Course(id = 1, semesterId = 1, name = "Cálculo Diferencial", location = "Aula 302", professor = "Dr. Smith", dayOfWeek = 1, startTime = "08:00", endTime = "10:00", isRemote = false)
                ),
                ClassSessionWithCourse(
                    session = ClassSession(courseId = 2, dayOfWeek = 1, startTime = "14:00", endTime = "16:00"),
                    course = Course(id = 2, semesterId = 1, name = "Física Mecánica", location = "Zoom Meet", professor = "Ing. Pérez", dayOfWeek = 1, startTime = "14:00", endTime = "16:00", isRemote = true)
                )
            ),
            pendingTasksCount = 5,
            priorities = listOf(
                TaskWithCourse(
                    task = Task(id = 1, courseId = 1, title = "Taller de Derivadas", dueDate = System.currentTimeMillis(), isPriority = true, status = "TODO", week = 3, description = "Resolver ejercicios del 1 al 10"),
                    course = Course(id = 1, semesterId = 1, name = "Cálculo Diferencial", location = "Aula 302", professor = "Dr. Smith", dayOfWeek = 1, startTime = "08:00", endTime = "10:00")
                ),
                TaskWithCourse(
                    task = Task(id = 2, courseId = 2, title = "Mapa Conceptual", dueDate = System.currentTimeMillis() + 90000000, isPriority = true, status = "TODO", week = 4, description = "Hacer un mapa sobre las leyes de Newton"),
                    course = Course(id = 2, semesterId = 1, name = "Física Mecánica", location = "Aula 101", professor = "Ing. Pérez", dayOfWeek = 2, startTime = "10:00", endTime = "12:00")
                )
            ),
            courses = listOf(
                CourseWithAverage(
                    Course(id = 1, semesterId = 1, name = "Cálculo Diferencial", code = "MATH101", location = "Aula 302", professor = "Dr. Smith", dayOfWeek = 1, startTime = "08:00", endTime = "10:00"),
                    4.2
                ),
                CourseWithAverage(
                    Course(id = 2, semesterId = 1, name = "Física Mecánica", code = "PHYS101", location = "Aula 101", professor = "Ing. Pérez", dayOfWeek = 2, startTime = "10:00", endTime = "12:00"),
                    3.8
                )
            )
        )
    }
}
