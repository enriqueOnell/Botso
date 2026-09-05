package com.onell.botso.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.Task // Adiós TaskEntity, hola Task puro
import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.viewmodel.KanbanViewModel
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalLocale
import com.onell.botso.ui.components.dialogs.AddEditTaskDialog
import com.onell.botso.ui.components.kanban.TaskCard
import com.onell.botso.ui.uistate.KanbanColumnInfo
import com.onell.botso.ui.uistate.KanbanUiEvent

@Composable
fun KanbanScreen(
    viewModel: KanbanViewModel = hiltViewModel()
) {
    // 1. Observamos el único estado de la interfaz
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }
    // 2. Cambiamos TaskEntity por Task
    var taskToEdit by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddDialog = true },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Añadir Tarea")
            }
        }
    ) { padding ->
        KanbanContent(
            // 3. Extraemos los datos del uiState unificado
            tasks = uiState.tasks,
            columns = uiState.columns,
            onTaskClick = { viewModel.onEvent(KanbanUiEvent.OnUpdateTaskStatus(it)) },
            onDeleteTask = { viewModel.onEvent(KanbanUiEvent.OnDeleteTask(it)) },
            onEditTask = { taskToEdit = it },
            modifier = Modifier.padding(padding)
        )
    }

    if (showAddDialog) {
        AddEditTaskDialog(
            courses = uiState.courses, // Sacamos los cursos del uiState
            onDismiss = { showAddDialog = false },
            onConfirm = { courseId, title, dueDate, isPriority, week, description ->
                viewModel.onEvent(KanbanUiEvent.OnAddTask(courseId, title, dueDate, isPriority, week, description))
                showAddDialog = false
            }
        )
    }

    taskToEdit?.let { task ->
        AddEditTaskDialog(
            courses = uiState.courses,
            task = task,
            onDismiss = { taskToEdit = null },
            onConfirm = { courseId, title, dueDate, isPriority, week, description ->
                viewModel.onEvent(KanbanUiEvent.OnUpdateTask(task, courseId, title, dueDate, isPriority, week, description))
                taskToEdit = null
            }
        )
    }
}

@Composable
fun KanbanContent(
    tasks: List<TaskWithCourse>,
    columns: List<KanbanColumnInfo>,
    // 4. Todas las funciones callback ahora exigen el modelo puro (Task)
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    val tasksByStatus = remember(tasks, columns) {
        columns.map { column ->
            column to tasks.filter { it.task.status == column.status }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        tasksByStatus.forEach { (column, columnTasks) ->
            item(key = "header_${column.status}") {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val displayTitle = when(column.title) {
                            "Hecho" -> "Terminado"
                            else -> column.title
                        }

                        Text(
                            text = displayTitle,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                            shape = CircleShape
                        ) {
                            Text(
                                text = columnTasks.size.toString(),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }

            if (columnTasks.isEmpty()) {
                item(key = "empty_${column.status}") {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "No hay tareas en esta sección",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                items(
                    items = columnTasks,
                    key = { "${it.task.id}_${column.status}" }
                ) { taskWithCourse ->
                    TaskCard(
                        taskWithCourse = taskWithCourse,
                        onClick = { onTaskClick(taskWithCourse.task) },
                        onDelete = { onDeleteTask(taskWithCourse.task) },
                        onEdit = { onEditTask(taskWithCourse.task) }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun KanbanPreview() {
    UniAppTheme {
        KanbanScreen()
    }
}
