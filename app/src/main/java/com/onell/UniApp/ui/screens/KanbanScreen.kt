package com.onell.UniApp.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.unit.sp
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
    LazyRow(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(columns) { column ->
            KanbanColumn(
                title = column.title,
                tasks = tasks.filter { it.task.status == column.status },
                onTaskClick = onTaskClick,
                onDeleteTask = onDeleteTask,
                onEditTask = onEditTask
            )
        }
    }
}

@Composable
fun KanbanColumn(
    title: String,
    tasks: List<TaskWithCourse>,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit
) {
    Column(modifier = Modifier.width(280.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = CircleShape
            ) {
                Text(
                    text = tasks.size.toString(),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(tasks) { taskWithCourse ->
                TaskCard(
                    taskWithCourse = taskWithCourse,
                    onClick = { onTaskClick(taskWithCourse.task) },
                    onDelete = { onDeleteTask(taskWithCourse.task) },
                    onEdit = { onEditTask(taskWithCourse.task) }
                )
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
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                            modifier = Modifier.widthIn(max = 150.dp)
                        )
                    }
                    
                    Surface(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape
                    ) {
                        Text(
                            text = "S${task.week}",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
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
                
                Spacer(modifier = Modifier.height(12.dp))
                
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
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = SimpleDateFormat("dd MMM", Locale.getDefault()).format(Date(task.dueDate)),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isDueToday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    if (task.isPriority) {
                        Icon(
                            Icons.Rounded.Star,
                            contentDescription = "Prioridad",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = {
                        showMenu = false
                        onEdit()
                    },
                    leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar") },
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
