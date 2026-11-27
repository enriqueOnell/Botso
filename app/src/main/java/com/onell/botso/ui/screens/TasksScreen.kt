package com.onell.botso.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Autorenew
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.components.dashboard.SectionEmptyState
import com.onell.botso.ui.components.dialogs.AddEditTaskDialog
import com.onell.botso.ui.components.tasks.TaskFilterSectionCard
import com.onell.botso.ui.components.tasks.TaskItem
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.TasksUiState
import com.onell.botso.ui.viewmodel.TasksViewModel
import java.time.LocalDate

/**
 * Pantalla de Tareas.
 *
 * Filtra las tareas por estado (Por Hacer, En Progreso, Hecha) y permite
 * avanzar de estado con un toque, editar o eliminar. El estado de los datos
 * vive en `TasksViewModel`; esta capa solo conserva el filtro seleccionado y la
 * tarea en edición.
 */
@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    uiState: TasksUiState,
    modifier: Modifier = Modifier
) {
    var taskToEdit by remember { mutableStateOf<Task?>(null) }
    val courses = (uiState as? TasksUiState.Success)?.courses ?: emptyList()

    TasksContent(
        uiState = uiState,
        onTaskClick = { task ->
            val nextStatus = when (task.status) {
                TaskStatus.TODO -> TaskStatus.IN_PROGRESS
                TaskStatus.IN_PROGRESS -> TaskStatus.DONE
                TaskStatus.DONE -> TaskStatus.TODO
            }
            viewModel.updateTask(task.copy(status = nextStatus))
        },
        onDeleteTask = { viewModel.deleteTask(it) },
        onEditTask = { taskToEdit = it },
        modifier = modifier
    )

    taskToEdit?.let { task ->
        AddEditTaskDialog(
            courses = courses,
            task = task,
            onDismiss = { taskToEdit = null },
            onConfirm = { courseId, title, dueDate, isPriority, week, description ->
                viewModel.updateTask(
                    task.copy(
                        courseId = courseId,
                        title = title,
                        dueDate = dueDate,
                        isPriority = isPriority,
                        week = week,
                        description = description
                    )
                )
                taskToEdit = null
            }
        )
    }
}

/**
 * Contenido puro de Tareas.
 *
 * No conoce el ViewModel: recibe el estado y los callbacks. `initialStatus`
 * solo define el filtro inicial (útil para previews); la interacción sigue
 * viviendo en el estado local de la pantalla.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TasksContent(
    uiState: TasksUiState,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    initialStatus: TaskStatus = TaskStatus.TODO,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is TasksUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is TasksUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        is TasksUiState.Success -> {
            var selectedStatus by remember(initialStatus) {
                mutableStateOf(initialStatus)
            }

            val filteredTasks = remember(uiState.tasksWithCourses, selectedStatus) {
                uiState.tasksWithCourses.filter { it.task.status == selectedStatus }
            }

            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "task-filters") {
                    TaskFilterSectionCard(
                        selectedStatus = selectedStatus,
                        taskCount = filteredTasks.size,
                        onStatusSelected = { selectedStatus = it }
                    )
                }

                if (filteredTasks.isEmpty()) {
                    item(key = "task-empty-${selectedStatus.name}") {
                        SectionEmptyState(
                            title = selectedStatus.emptyTitle,
                            subtitle = selectedStatus.emptySubtitle,
                            icon = selectedStatus.emptyIcon
                        )
                    }
                } else {
                    items(
                        items = filteredTasks,
                        key = { it.task.id }
                    ) { courseWithTask ->
                        TaskItem(
                            courseWithTask = courseWithTask,
                            onClick = { onTaskClick(courseWithTask.task) },
                            onDelete = { onDeleteTask(courseWithTask.task) },
                            onEdit = { onEditTask(courseWithTask.task) }
                        )
                    }
                }
            }
        }
    }
}

private val TaskStatus.emptyTitle: String
    get() = when (this) {
        TaskStatus.TODO -> "¡Todo al día!"
        TaskStatus.IN_PROGRESS -> "Nada en progreso"
        TaskStatus.DONE -> "Aún no hay tareas completadas"
    }

private val TaskStatus.emptySubtitle: String
    get() = when (this) {
        TaskStatus.TODO -> "No tienes tareas por hacer. Crea una con el botón +."
        TaskStatus.IN_PROGRESS -> "Toca una tarea por hacer para empezar a trabajarla."
        TaskStatus.DONE -> "Marca una tarea como hecha para verla aquí."
    }

private val TaskStatus.emptyIcon: ImageVector
    get() = when (this) {
        TaskStatus.TODO -> Icons.Rounded.TaskAlt
        TaskStatus.IN_PROGRESS -> Icons.Rounded.Autorenew
        TaskStatus.DONE -> Icons.Rounded.CheckCircle
    }

@Preview(name = "Tasks Light", showBackground = true)
@Preview(
    name = "Tasks Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TasksContentPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TasksContent(
                uiState = TasksUiState.Success(tasksWithCourses = previewTasks()),
                onTaskClick = {},
                onDeleteTask = {},
                onEditTask = {}
            )
        }
    }
}

@Preview(name = "Tasks In Progress Light", showBackground = true)
@Preview(
    name = "Tasks In Progress Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TasksContentInProgressPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TasksContent(
                uiState = TasksUiState.Success(tasksWithCourses = previewTasks()),
                onTaskClick = {},
                onDeleteTask = {},
                onEditTask = {},
                initialStatus = TaskStatus.IN_PROGRESS
            )
        }
    }
}

@Preview(name = "Tasks Done Light", showBackground = true)
@Preview(
    name = "Tasks Done Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TasksContentDonePreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TasksContent(
                uiState = TasksUiState.Success(tasksWithCourses = previewTasks()),
                onTaskClick = {},
                onDeleteTask = {},
                onEditTask = {},
                initialStatus = TaskStatus.DONE
            )
        }
    }
}

@Preview(name = "Tasks Empty Light", showBackground = true)
@Preview(
    name = "Tasks Empty Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TasksContentEmptyPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TasksContent(
                uiState = TasksUiState.Success(),
                onTaskClick = {},
                onDeleteTask = {},
                onEditTask = {}
            )
        }
    }
}

private fun previewTasks(): List<CourseWithTask> = listOf(
    previewTask(
        id = "todo-1",
        title = "Taller de Redes OAuth2",
        courseName = "Redes de Computadores",
        status = TaskStatus.TODO,
        week = 14,
        dueDate = LocalDate.of(2026, 12, 2),
        description = "Subir captura de Wireshark y el repositorio antes de medianoche.",
        hasAttachment = true
    ),
    previewTask(
        id = "todo-2",
        title = "Parcial Calidad de Software",
        courseName = "Calidad de Software",
        status = TaskStatus.TODO,
        week = 15,
        dueDate = LocalDate.of(2026, 11, 28),
        description = "Repasar criterios de calidad y métricas del proyecto.",
        isPriority = true
    ),
    previewTask(
        id = "in-progress",
        title = "Informe de laboratorio",
        courseName = "Sistemas Operativos",
        status = TaskStatus.IN_PROGRESS,
        week = 13,
        dueDate = LocalDate.of(2026, 12, 4),
        description = "Documentar la práctica de planificación de procesos."
    ),
    previewTask(
        id = "done",
        title = "Quiz de Bases de Datos",
        courseName = "Bases de Datos",
        status = TaskStatus.DONE,
        week = 12,
        dueDate = LocalDate.of(2026, 11, 20),
        description = "Entregado en clase."
    )
)

private fun previewTask(
    id: String,
    title: String,
    courseName: String,
    status: TaskStatus,
    week: Int,
    dueDate: LocalDate,
    description: String,
    isPriority: Boolean = false,
    hasAttachment: Boolean = false,
): CourseWithTask = CourseWithTask(
    task = Task(
        id = id,
        courseId = "course-$id",
        title = title,
        dueDate = dueDate,
        isPriority = isPriority,
        hasAttachment = hasAttachment,
        status = status,
        week = week,
        description = description
    ),
    course = Course(
        id = "course-$id",
        semesterId = "semester-preview",
        name = courseName,
        professor = "Profesor"
    )
)
