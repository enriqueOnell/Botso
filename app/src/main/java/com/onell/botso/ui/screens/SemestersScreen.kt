package com.onell.botso.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.ui.components.semester.SemesterCard
import com.onell.botso.ui.uistate.SemestersUiState
import com.onell.botso.ui.viewmodel.SemestersViewModel
import java.io.File

@Composable
fun SemestersScreen(
    viewModel: SemestersViewModel,
    uiState: SemestersUiState,
    onNavigateToCourseGrades: (String) -> Unit,
    onAddCourseClicked: (String) -> Unit,
    onEditSemesterClicked: (Semester) -> Unit,
    onEditCourseClicked: (CourseWithSession) -> Unit,
    onDeleteCourseClicked: (Course) -> Unit,
    onDeleteSemesterClicked: (Semester) -> Unit
) {
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

    SemestersContent(
        uiState = uiState,
        onNavigateToCourseGrades = onNavigateToCourseGrades,
        onAddCourseClicked = onAddCourseClicked,
        onEditSemesterClicked = onEditSemesterClicked,
        onEditCourseClicked = onEditCourseClicked,
        onDeleteCourseClicked = onDeleteCourseClicked,
        onDeleteSemesterClicked = onDeleteSemesterClicked,
        onExportPdf = { viewModel.exportSemesterToPdf(it) }
    )
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun SemestersContent(
    uiState: SemestersUiState,
    onNavigateToCourseGrades: (String) -> Unit,
    onAddCourseClicked: (String) -> Unit,
    onEditSemesterClicked: (Semester) -> Unit,
    onEditCourseClicked: (CourseWithSession) -> Unit,
    onDeleteCourseClicked: (Course) -> Unit,
    onDeleteSemesterClicked: (Semester) -> Unit,
    onExportPdf: (Semester) -> Unit
) {
    when (uiState) {
        is SemestersUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                LoadingIndicator()
            }
        }
        is SemestersUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is SemestersUiState.Success -> {
            if (uiState.semesterWithCourses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No hay semestres registrados",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(uiState.semesterWithCourses) { semesterData ->
                        SemesterCard(
                            semesterData = semesterData,
                            onCourseClick = onNavigateToCourseGrades,
                            onAddCourse = { onAddCourseClicked(semesterData.semester.id) },
                            onEditSemester = onEditSemesterClicked,
                            onEditCourse = onEditCourseClicked,
                            onDeleteCourse = onDeleteCourseClicked,
                            onDeleteSemester = onDeleteSemesterClicked,
                            onExportPdf = onExportPdf
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SemestersScreenPreview() {
    SemestersContent(
        uiState = SemestersUiState.Success(emptyList()),
        onNavigateToCourseGrades = {},
        onAddCourseClicked = {},
        onEditSemesterClicked = {},
        onEditCourseClicked = {},
        onDeleteCourseClicked = {},
        onDeleteSemesterClicked = {},
        onExportPdf = {}
    )
}