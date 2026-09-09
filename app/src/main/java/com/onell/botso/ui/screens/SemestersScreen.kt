package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.ui.components.dialogs.AddEditCourseDialog
import com.onell.botso.ui.components.dialogs.AddEditSemesterDialog
import com.onell.botso.ui.components.dialogs.ConfirmDeleteDialog
import com.onell.botso.ui.components.semester.SemesterCard
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.SemestersUiEvent
import com.onell.botso.ui.viewmodel.SemestersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemestersScreen(
    viewModel: SemestersViewModel = hiltViewModel(),
    onNavigateToCourseGrades: (String) -> Unit = {}
) {
    // 1. Observamos el estado centralizado purgado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val semestersWithStats = uiState.semestersWithStats

    var showAddSemesterDialog by remember { mutableStateOf(false) }
    var showAddCourseDialogForSemesterId by remember { mutableStateOf<String?>(null) }

    // 2. Erradicamos los "Entity" de las variables de estado
    var semesterToEdit by remember { mutableStateOf<Semester?>(null) }
    var courseToEdit by remember { mutableStateOf<Course?>(null) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }
    var semesterToDelete by remember { mutableStateOf<Semester?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión Académica", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddSemesterDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Semestre")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(semestersWithStats) { stats ->
                SemesterCard(
                    stats = stats,
                    onCourseClick = onNavigateToCourseGrades,
                    onAddCourse = { showAddCourseDialogForSemesterId = stats.semester.id },
                    onEditSemester = { semesterToEdit = it },
                    onEditCourse = { courseToEdit = it },
                    onDeleteCourse = { courseToDelete = it },
                    onDeleteSemester = { semesterToDelete = it }
                )
            }
        }
    }

    if (showAddSemesterDialog) {
        AddEditSemesterDialog(
            onDismiss = { showAddSemesterDialog = false },
            onConfirm = { id, name, start, end ->
                viewModel.onEvent(SemestersUiEvent.OnAddSemester(id, name, start, end, true))
                showAddSemesterDialog = false
            }
        )
    }

    semesterToEdit?.let { semester ->
        AddEditSemesterDialog(
            semester = semester,
            onDismiss = { semesterToEdit = null },
            onConfirm = { _ , name, start, end ->
                viewModel.onEvent(
                    SemestersUiEvent.OnUpdateSemester(
                        semester,
                        name,
                        start,
                        end,
                        semester.isActive
                    )
                )
                semesterToEdit = null
            }
        )
    }

    showAddCourseDialogForSemesterId?.let { semesterId ->
        AddEditCourseDialog(
            onDismiss = { showAddCourseDialogForSemesterId = null },
            onConfirm = { id, name, dayOfWeek, start, end, professor, location, isRemote ->
                viewModel.onEvent(
                    SemestersUiEvent.OnAddCourse(
                        id,
                        semesterId,
                        name,
                        dayOfWeek,
                        start,
                        end,
                        professor,
                        "#4f378a",
                        location,
                        isRemote
                    )
                )
                showAddCourseDialogForSemesterId = null
            }
        )
    }

    courseToEdit?.let { course ->
        AddEditCourseDialog(
            course = course,
            onDismiss = { courseToEdit = null },
            onConfirm = { id, name, dayOfWeek, start, end, professor, location, isRemote ->
                viewModel.onEvent(
                    SemestersUiEvent.OnEditCourse(
                        course,
                        name,
                        dayOfWeek,
                        start,
                        end,
                        professor,
                        course.colorHex,
                        location,
                        isRemote
                    )
                )
                courseToEdit = null
            }
        )
    }

    courseToDelete?.let { course ->
        ConfirmDeleteDialog(
            title = "Eliminar Curso",
            message = "¿Estás seguro de que deseas eliminar el curso '${course.name}'? Esta acción no se puede deshacer.",
            onConfirm = {
                viewModel.onEvent(SemestersUiEvent.OnDeleteCourseConfirm(course))
                courseToDelete = null
            },
            onDismiss = { courseToDelete = null }
        )
    }

    semesterToDelete?.let { semester ->
        ConfirmDeleteDialog(
            title = "Eliminar Semestre",
            message = "¿Estás seguro de que deseas eliminar el semestre '${semester.name}'? Se eliminarán todos los cursos y notas asociados.",
            onConfirm = {
                viewModel.onEvent(SemestersUiEvent.OnDeleteSemester(semester))
                semesterToDelete = null
            },
            onDismiss = { semesterToDelete = null }
        )
    }
}


@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun SemestersScreenPreview() {
    BotsoTheme {
        SemestersScreen()
    }
}
