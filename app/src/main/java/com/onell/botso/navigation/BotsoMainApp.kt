package com.onell.botso.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalFloatingToolbar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.components.dialogs.AddEditCourseDialog
import com.onell.botso.ui.components.dialogs.AddEditSemesterDialog
import com.onell.botso.ui.components.dialogs.AddEditTaskDialog
import com.onell.botso.ui.components.dialogs.ConfirmDeleteDialog
import com.onell.botso.ui.screens.CourseGradesScreen
import com.onell.botso.ui.screens.DashboardScreen
import com.onell.botso.ui.screens.SemestersScreen
import com.onell.botso.ui.screens.TasksScreen
import com.onell.botso.ui.uistate.TasksUiState
import com.onell.botso.ui.viewmodel.SemestersViewModel
import com.onell.botso.ui.viewmodel.TasksViewModel
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BotsoMainApp(
    taskViewModel: TasksViewModel = hiltViewModel(),
    semesterViewModel: SemestersViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val taskUiState by taskViewModel.uiState.collectAsStateWithLifecycle()
    val semesterUiState by semesterViewModel.uiState.collectAsStateWithLifecycle()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showTaskDialog by remember { mutableStateOf(false) }
    var showSemesterDialog by remember { mutableStateOf(false) }
    var showAddCourseDialogForSemesterId by remember { mutableStateOf<String?>(null) }
    var semesterToEdit by remember { mutableStateOf<Semester?>(null) }
    var courseWithSessionToEdit by remember { mutableStateOf<CourseWithSession?>(null) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }
    var semesterToDelete by remember { mutableStateOf<Semester?>(null) }

    val courses = (taskUiState as? TasksUiState.Success)?.courses ?: emptyList()

    Scaffold(
        bottomBar = {
            val isDashboard = currentDestination?.hasRoute<Route.Dashboard>() == true
            val isTasks = currentDestination?.hasRoute<Route.Tasks>() == true
            val isSemesters = currentDestination?.hasRoute<Route.Semesters>() == true

            val showBottomBar = isDashboard || isTasks || isSemesters

            if (showBottomBar) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    HorizontalFloatingToolbar(
                        expanded = true,
                        floatingActionButton = {
                            if (isTasks || isSemesters) {
                                FloatingActionButton(
                                    onClick = {
                                        if (isTasks) showTaskDialog = true
                                        if (isSemesters) showSemesterDialog = true
                                    }
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                                }
                            }
                        },
                        content = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Botón Dashboard
                                Button(
                                    onClick = {
                                        if (!isDashboard) {
                                            navController.navigate(Route.Dashboard) {
                                                popUpTo(Route.Dashboard) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isDashboard) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = if (isDashboard) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    ),
                                    contentPadding = PaddingValues(12.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Dashboard,
                                        contentDescription = "Dashboard",
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                // Botón Tareas
                                Button(
                                    onClick = {
                                        if (!isTasks) {
                                            navController.navigate(Route.Tasks) {
                                                popUpTo(Route.Dashboard) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isTasks) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = if (isTasks) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    ),
                                    contentPadding = PaddingValues(12.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.TaskAlt,
                                        contentDescription = "Tareas",
                                        modifier = Modifier.size(26.dp)
                                    )
                                }

                                // Botón Semestres
                                Button(
                                    onClick = {
                                        if (!isSemesters) {
                                            navController.navigate(Route.Semesters) {
                                                popUpTo(Route.Dashboard) { saveState = true }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isSemesters) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
                                        contentColor = if (isSemesters) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface
                                    ),
                                    contentPadding = PaddingValues(12.dp),
                                    modifier = Modifier.padding(horizontal = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = "Semestres",
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->

        NavHost(
            navController = navController,
            startDestination = Route.Dashboard,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            composable<Route.Dashboard> {
                DashboardScreen()
            }

            composable<Route.Tasks> {
                TasksScreen(taskViewModel, taskUiState)
            }

            composable<Route.Semesters> {
                SemestersScreen(
                    viewModel = semesterViewModel,
                    uiState = semesterUiState,
                    onNavigateToCourseGrades = { courseId ->
                        navController.navigate(Route.CourseGrades(courseId = courseId))
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
            }

            composable<Route.CourseGrades> { backStackEntry ->
                val routeArgs = backStackEntry.toRoute<Route.CourseGrades>()
                CourseGradesScreen(courseId = routeArgs.courseId)
            }
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