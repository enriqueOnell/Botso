package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.components.tasks.TaskCard
import com.onell.botso.ui.uistate.TasksUiState
import com.onell.botso.ui.viewmodel.TasksViewModel
import java.time.LocalDate

@Composable
fun TasksScreen(
    viewModel: TasksViewModel,
    uiState: TasksUiState
) {

    var taskToEdit by remember { mutableStateOf<Task?>(null) }

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
            modifier = Modifier.padding(16.dp)
        )
    }

@Composable
fun TasksContent(
    uiState: TasksUiState,
    onTaskClick: (Task) -> Unit,
    onDeleteTask: (Task) -> Unit,
    onEditTask: (Task) -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is TasksUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is TasksUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        is TasksUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(4.dp),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(uiState.tasksWithCourses) { curso ->
                    Spacer(modifier = Modifier.padding(8.dp))
                    TaskCard(
                        courseWithTask = curso,
                        onClick = { onTaskClick(curso.task) },
                        onDelete = { onDeleteTask(curso.task) },
                        onEdit = { onEditTask(curso.task) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TasksContentPreview() {
    TasksContent(

        uiState = TasksUiState.Success(
            listOf(CourseWithTask(
                task = Task(
                    id = "id",
                    courseId = "courseId",
                    title = "title",
                    dueDate = LocalDate.now(),
                    isPriority = true,
                    hasAttachment = true,
                    status = TaskStatus.TODO,
                    week = 3,
                    description = ""
                ),
                course = Course(
                    id = "id2",
                    semesterId = "semesterId",
                    name = "name",
                    professor = "professor"
                )
            ),
                CourseWithTask(
                    task = Task(
                        id = "id",
                        courseId = "courseId",
                        title = "title",
                        dueDate = LocalDate.now(),
                        isPriority = true,
                        hasAttachment = true,
                        status = TaskStatus.TODO,
                        week = 3,
                        description = "Tengo que hacer esta tarea urgentemente si no me puedo tirar el año"
                    ),
                    course = Course(
                        id = "id2",
                        semesterId = "semesterId",
                        name = "name",
                        professor = "professor"
                    )
                ),
                CourseWithTask(
                    task = Task(
                        id = "id",
                        courseId = "courseId",
                        title = "title",
                        dueDate = LocalDate.now(),
                        isPriority = true,
                        hasAttachment = true,
                        status = TaskStatus.TODO,
                        week = 3,
                        description = ""
                    ),
                    course = Course(
                        id = "id2",
                        semesterId = "semesterId",
                        name = "name",
                        professor = "professor"
                    )
                ))
        ),
        onTaskClick = {},
        onDeleteTask = {},
        onEditTask = {}
    )
}
