package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import android.content.Intent
import androidx.compose.runtime.LaunchedEffect
import java.io.File
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.ui.components.dialogs.AddEditCourseDialog
import com.onell.botso.ui.components.dialogs.AddEditSemesterDialog
import com.onell.botso.ui.components.dialogs.ConfirmDeleteDialog
import com.onell.botso.ui.components.semester.SemesterCard
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.SemestersUiState
import com.onell.botso.ui.viewmodel.SemestersViewModel

@Composable
fun SemestersScreen(
    viewModel: SemestersViewModel = hiltViewModel(),
    onNavigateToCourseGrades: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SemesterContent(
        viewModel,
        uiState,
        onNavigateToCourseGrades
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterContent(
    viewModel: SemestersViewModel,
    uiState: SemestersUiState,
    onNavigateToCourseGrades: (String) -> Unit
) {
    var showAddSemesterDialog by remember { mutableStateOf(false) }
    var showAddCourseDialogForSemesterId by remember { mutableStateOf<String?>(null) }

    var semesterToEdit by remember { mutableStateOf<Semester?>(null) }
    var sessionWithCourseToEdit by remember { mutableStateOf<ClassSessionWithCourse?>(null) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }
    var semesterToDelete by remember { mutableStateOf<Semester?>(null) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.pdfGeneratedEvent.collect { pdfPath ->
            val file = File(pdfPath)
            if (file.exists()) {
                val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "application/pdf"
                    putExtra(Intent.EXTRA_STREAM, uri)
                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                }
                context.startActivity(Intent.createChooser(intent, "Compartir Reporte"))
            }
        }
    }

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

        when (uiState) {
            is SemestersUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            is SemestersUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            is SemestersUiState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.semesterWithCourses) { semesterData ->
                        SemesterCard(
                            semesterData = semesterData,
                            onCourseClick = onNavigateToCourseGrades,
                            onAddCourse = {
                                showAddCourseDialogForSemesterId = semesterData.semester.id
                            },
                            onEditSemester = { semesterToEdit = it },
                            onEditCourse = { sessionWithCourseToEdit = it },
                            onDeleteCourse = { courseToDelete = it },
                            onDeleteSemester = { semesterToDelete = it },
                            onExportPdf = { viewModel.exportSemesterToPdf(it) }
                        )
                    }
                }
            }
        }

        if (showAddSemesterDialog) {
            AddEditSemesterDialog(
                onDismiss = { showAddSemesterDialog = false },
                onConfirm = { nuevoSemestre ->
                    viewModel.addSemester(nuevoSemestre)
                    showAddSemesterDialog = false
                }
            )
        }

        semesterToEdit?.let { semester ->
            AddEditSemesterDialog(
                semester = semester,
                onDismiss = { semesterToEdit = null },
                onConfirm = { semestreActualizado ->
                    viewModel.updateSemester(semestreActualizado)
                    semesterToEdit = null
                }
            )
        }

        showAddCourseDialogForSemesterId?.let { semesterId ->
            AddEditCourseDialog(
                semesterId = semesterId,
                onDismiss = { showAddCourseDialogForSemesterId = null },
                onConfirm = { newCourse, newSession ->
                    viewModel.addCourse(newCourse, newSession)
                    showAddCourseDialogForSemesterId = null
                }
            )
        }

        sessionWithCourseToEdit?.let { sessionWithCourse ->
            AddEditCourseDialog(
                sessionWithCourse = sessionWithCourse,
                onDismiss = { sessionWithCourseToEdit = null },
                onConfirm = { updatedCourse, updatedSession ->
                    viewModel.updateCourse(updatedCourse, updatedSession)
                    sessionWithCourseToEdit = null
                }
            )
        }

        courseToDelete?.let { course ->
            ConfirmDeleteDialog(
                title = "Eliminar Curso",
                message = "¿Estás seguro de que deseas eliminar el curso '${course.name}'? Esta acción no se puede deshacer.",
                onConfirm = {
                    viewModel.deleteCourse(course)
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
                    viewModel.deleteSemester(semester)
                    semesterToDelete = null
                },
                onDismiss = { semesterToDelete = null }
            )
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun SemestersScreenPreview() {
    BotsoTheme {
        SemestersScreen()
    }
}
