package com.onell.botso.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)

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

    when (uiState) {
        is SemestersUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is SemestersUiState.Error -> {
            Box(modifier = Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text(text = uiState.message, color = MaterialTheme.colorScheme.error)
            }
        }
        is SemestersUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(16.dp),
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
                        onExportPdf = { viewModel.exportSemesterToPdf(it) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SemestersScreenPreview() {

}
