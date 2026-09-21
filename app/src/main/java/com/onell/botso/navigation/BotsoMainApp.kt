package com.onell.botso.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.components.dialogs.*
import com.onell.botso.ui.screens.*
import com.onell.botso.ui.uistate.TasksUiState
import com.onell.botso.ui.viewmodel.SemestersViewModel
import com.onell.botso.ui.viewmodel.TasksViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotsoMainApp(
    taskViewModel: TasksViewModel = hiltViewModel(),
    semesterViewModel: SemestersViewModel = hiltViewModel()
) {
    val taskUiState by taskViewModel.uiState.collectAsStateWithLifecycle()
    val semesterUiState by semesterViewModel.uiState.collectAsStateWithLifecycle()

    val backStack = remember { mutableStateListOf<Route>(Route.Dashboard) }

    var showTaskDialog by remember { mutableStateOf(false) }
    var showSemesterDialog by remember { mutableStateOf(false) } // Usado por el FAB para agregar semestre
    var showAddCourseDialogForSemesterId by remember { mutableStateOf<String?>(null) }
    var semesterToEdit by remember { mutableStateOf<Semester?>(null) }
    var courseWithSessionToEdit by remember { mutableStateOf<CourseWithSession?>(null) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }
    var semesterToDelete by remember { mutableStateOf<Semester?>(null) }

    val courses = (taskUiState as? TasksUiState.Success)?.courses ?: emptyList()

    BackHandler(enabled = backStack.size > 1 ) {
        backStack.removeAt(backStack.lastIndex)
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
            contentAlignment = Alignment.Center
        ) {
            val currentRoute = backStack.last()

            when (currentRoute) {
                is Route.Dashboard -> DashboardScreen()
                is Route.Tasks -> TasksScreen(taskViewModel, taskUiState)
                is Route.Semesters -> SemestersScreen(
                    viewModel = semesterViewModel,
                    uiState = semesterUiState,
                    onNavigateToCourseGrades = { courseId ->
                        backStack.add(Route.CourseGrades(courseId))
                    },
                    onAddCourseClicked = { semesterId ->
                        showAddCourseDialogForSemesterId = semesterId
                    },
                    onEditSemesterClicked = { semester -> semesterToEdit = semester },
                    onEditCourseClicked = { courseSession ->
                        courseWithSessionToEdit = courseSession
                    },
                    onDeleteCourseClicked = { course -> courseToDelete = course },
                    onDeleteSemesterClicked = { semester -> semesterToDelete = semester }
                )
                is Route.CourseGrades -> CourseGradesScreen(courseId = currentRoute.courseId)
            }

            if (currentRoute !is Route.CourseGrades) {

                HorizontalFloatingToolbar(
                    modifier = Modifier
                        .align(BottomCenter)
                        .padding(bottom = 8.dp),
                    expanded = true,

                    floatingActionButton = {
                        if (currentRoute is Route.Tasks || currentRoute is Route.Semesters) {
                            FloatingActionButton(
                                onClick = {
                                    when (currentRoute) {
                                        is Route.Tasks -> showTaskDialog = true
                                        is Route.Semesters -> showSemesterDialog = true
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Agregar")
                            }
                        }
                    },

                    content = {
                        val isDashboardSelected = currentRoute is Route.Dashboard
                        val isTasksSelected = currentRoute is Route.Tasks
                        val isSemestersSelected = currentRoute is Route.Semesters

                        TextButton(
                            onClick = { if (!isDashboardSelected) backStack.add(Route.Dashboard) },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (isDashboardSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = if (isDashboardSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            ),
                            contentPadding = if (isDashboardSelected) ButtonDefaults.TextButtonContentPadding else PaddingValues(0.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.Dashboard, contentDescription = "Dashboard")
                            if (isDashboardSelected) {
                                Text(
                                    text = "Botso",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        TextButton(
                            onClick = { if (!isTasksSelected) backStack.add(Route.Tasks) },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (isTasksSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = if (isTasksSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            ),
                            contentPadding = if (isTasksSelected) ButtonDefaults.TextButtonContentPadding else PaddingValues(0.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.Default.Task, contentDescription = "Tasks")
                            if (isTasksSelected) {
                                Text(
                                    text = "Tareas",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }

                        TextButton(
                            onClick = { if (!isSemestersSelected) backStack.add(Route.Semesters) },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = if (isSemestersSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                contentColor = if (isSemestersSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                            ),
                            contentPadding = if (isSemestersSelected) ButtonDefaults.TextButtonContentPadding else PaddingValues(0.dp),
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Semesters")
                            if (isSemestersSelected) {
                                Text(
                                    text = "Semestres",
                                    modifier = Modifier.padding(start = 8.dp),
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }
                )
            }



            if (showTaskDialog) {
                AddEditTaskDialog(
                    courses = courses,
                    onDismiss = { showTaskDialog = false },
                    onConfirm = { courseId, title, dueDate, isPriority, week, description ->
                        taskViewModel.insertTask(
                            Task(
                                id = UUID.randomUUID().toString(),
                                courseId = courseId,
                                title = title,
                                description = description,
                                dueDate = dueDate,
                                isPriority = isPriority,
                                week = week,
                                status = TaskStatus.TODO
                            )
                        )
                        showTaskDialog = false
                    }
                )
            }

            if (showSemesterDialog) {
                AddEditSemesterDialog(
                    onDismiss = { showSemesterDialog = false },
                    onConfirm = { nuevoSemestre ->
                        semesterViewModel.addSemester(nuevoSemestre)
                        showSemesterDialog = false
                    }
                )
            }

            semesterToEdit?.let { semester ->
                AddEditSemesterDialog(
                    semester = semester,
                    onDismiss = { semesterToEdit = null },
                    onConfirm = { semestreActualizado ->
                        semesterViewModel.updateSemester(semestreActualizado)
                        semesterToEdit = null
                    }
                )
            }

            showAddCourseDialogForSemesterId?.let { semesterId ->
                AddEditCourseDialog(
                    semesterId = semesterId,
                    onDismiss = { showAddCourseDialogForSemesterId = null },
                    onConfirm = { newCourse, newSession ->
                        semesterViewModel.addCourse(newCourse, newSession)
                        showAddCourseDialogForSemesterId = null
                    }
                )
            }

            courseWithSessionToEdit?.let { courseWithSession ->
                AddEditCourseDialog(
                    courseWithSession = courseWithSession,
                    onDismiss = { courseWithSessionToEdit = null },
                    onConfirm = { updatedCourse, updatedSession ->
                        semesterViewModel.updateCourse(updatedCourse, updatedSession)
                        courseWithSessionToEdit = null
                    }
                )
            }

            courseToDelete?.let { course ->
                ConfirmDeleteDialog(
                    title = "Eliminar Curso",
                    message = "¿Estás seguro de que deseas eliminar el curso '${course.name}'?",
                    onConfirm = {
                        semesterViewModel.deleteCourse(course)
                        courseToDelete = null
                    },
                    onDismiss = { courseToDelete = null }
                )
            }

            semesterToDelete?.let { semester ->
                ConfirmDeleteDialog(
                    title = "Eliminar Semestre",
                    message = "¿Estás seguro de que deseas eliminar el semestre '${semester.name}'?",
                    onConfirm = {
                        semesterViewModel.deleteSemester(semester)
                        semesterToDelete = null
                    },
                    onDismiss = { semesterToDelete = null }
                )
            }
        }
    }
}