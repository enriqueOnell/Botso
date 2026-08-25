package com.onell.UniApp.ui.screens

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
import com.onell.UniApp.data.local.entity.Task
import com.onell.UniApp.domain.model.TaskWithCourse
import com.onell.UniApp.ui.components.AddEditTaskDialog
import com.onell.UniApp.ui.theme.UniAppTheme
import com.onell.UniApp.ui.viewmodel.KanbanUiEvent
import com.onell.UniApp.ui.viewmodel.KanbanViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun KanbanScreen(
    viewModel: KanbanViewModel = hiltViewModel()
) {
    val tasks by viewModel.tasks.collectAsState()
    val courses by viewModel.courses.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }
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
            tasks = tasks,
            columns = viewModel.columns,
            onTaskClick = { viewModel.onEvent(KanbanUiEvent.OnUpdateTaskStatus(it)) },
            onDeleteTask = { viewModel.onEvent(KanbanUiEvent.OnDeleteTask(it)) },
            onEditTask = { taskToEdit = it },
            modifier = Modifier.padding(padding)
        )
    }

    if (showAddDialog) {
        AddEditTaskDialog(
            courses = courses,
            onDismiss = { showAddDialog = false },
            onConfirm = { courseId, title, dueDate, isPriority, week, description ->
                viewModel.onEvent(KanbanUiEvent.OnAddTask(courseId, title, dueDate, isPriority, week, description))
                showAddDialog = false
            }
        )
    }

    taskToEdit?.let { task ->
        AddEditTaskDialog(
            courses = courses,
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
    columns: List<com.onell.UniApp.ui.viewmodel.KanbanColumnInfo>,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    // Pre-calculamos las tareas por columna para evitar filtrados innecesarios durante la recomposición
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
            // Encabezado de sección (Por Hacer, En Progreso, Terminado)
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
                        // Mapeo dinámico para asegurar que se muestre "Terminado" si el ViewModel dice "Hecho"
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

            // Lista de tarjetas para este estado
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
            
            // Espaciador entre secciones
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskCard(
    taskWithCourse: TaskWithCourse,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    val task = taskWithCourse.task
    val course = taskWithCourse.course
    var showMenu by remember { mutableStateOf(false) }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showMenu = true }
            ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Rounded.Book,
                            contentDescription = null,
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = course.name,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.widthIn(max = 200.dp)
                        )
                    }
                    
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "S${task.week}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(14.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        val isDueToday = task.dueDate < System.currentTimeMillis() + 86400000
                        Icon(
                            if (isDueToday) Icons.Rounded.NotificationImportant else Icons.Rounded.CalendarToday,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = if (isDueToday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault()).format(Date(task.dueDate)),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isDueToday) FontWeight.Bold else FontWeight.Normal,
                            color = if (isDueToday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (task.isPriority) {
                        Surface(
                            color = MaterialTheme.colorScheme.tertiaryContainer,
                            shape = CircleShape,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Rounded.Star,
                                    contentDescription = "Prioridad",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                                )
                            }
                        }
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar Tarea") },
                    onClick = {
                        showMenu = false
                        onEdit()
                    },
                    leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar Tarea") },
                    onClick = {
                        showMenu = false
                        onDelete()
                    },
                    leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null) },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.error
                    )
                )
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
