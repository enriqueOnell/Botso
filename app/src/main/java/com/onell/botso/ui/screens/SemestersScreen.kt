package com.onell.botso.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.ui.components.dashboard.SectionEmptyState
import com.onell.botso.ui.components.semesters.SemesterSectionCard
import com.onell.botso.ui.components.semesters.previewCourse
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.SemestersUiState
import com.onell.botso.ui.viewmodel.SemestersViewModel
import java.time.LocalDate

/**
 * Pantalla de Semestres.
 *
 * Lista los semestres registrados con sus materias y horarios, permitiendo
 * navegar a las notas de cada curso y delegar a `BotsoMainApp` los diálogos
 * de creación, edición y eliminación de semestres y cursos.
 */
@Composable
fun SemestersScreen(
    viewModel: SemestersViewModel,
    uiState: SemestersUiState,
    onNavigateToCourseGrades: (String) -> Unit,
    onAddCourseClicked: (String) -> Unit,
    onEditSemesterClicked: (Semester) -> Unit,
    onEditCourseClicked: (CourseWithSession) -> Unit,
    onDeleteCourseClicked: (Course) -> Unit,
    onDeleteSemesterClicked: (Semester) -> Unit,
    modifier: Modifier = Modifier
) {
    SemestersContent(
        uiState = uiState,
        onNavigateToCourseGrades = onNavigateToCourseGrades,
        onAddCourseClicked = onAddCourseClicked,
        onEditSemesterClicked = onEditSemesterClicked,
        onEditCourseClicked = onEditCourseClicked,
        onDeleteCourseClicked = onDeleteCourseClicked,
        onDeleteSemesterClicked = onDeleteSemesterClicked,
        modifier = modifier
    )
}

/**
 * Contenido puro de Semestres.
 *
 * No conoce el ViewModel: recibe el estado y los callbacks de navegación y CRUD.
 * La raíz queda transparente para respetar el fondo base de la app y la lista
 * reserva espacio inferior para la `HorizontalFloatingToolbar`.
 */
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
    modifier: Modifier = Modifier
) {
    when (uiState) {
        is SemestersUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is SemestersUiState.Error -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }

        is SemestersUiState.Success -> {
            LazyColumn(
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (uiState.semesterWithCourses.isEmpty()) {
                    item(key = "semesters-empty") {
                        SectionEmptyState(
                            title = "Sin semestres registrados",
                            subtitle = "Crea tu primer semestre con el botón + para organizar tus materias.",
                            icon = Icons.Rounded.School
                        )
                    }
                } else {
                    items(
                        items = uiState.semesterWithCourses,
                        key = { it.semester.id }
                    ) { semesterData ->
                        SemesterSectionCard(
                            semesterData = semesterData,
                            onCourseClick = onNavigateToCourseGrades,
                            onAddCourse = { onAddCourseClicked(semesterData.semester.id) },
                            onEditSemester = onEditSemesterClicked,
                            onEditCourse = onEditCourseClicked,
                            onDeleteCourse = onDeleteCourseClicked,
                            onDeleteSemester = onDeleteSemesterClicked
                        )
                    }
                }
            }
        }
    }
}

@Preview(name = "Semesters Light", showBackground = true)
@Preview(
    name = "Semesters Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SemestersContentPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SemestersContent(
                uiState = SemestersUiState.Success(
                    allSemester = previewSemesters().map { it.semester },
                    semesterWithCourses = previewSemesters()
                ),
                onNavigateToCourseGrades = {},
                onAddCourseClicked = {},
                onEditSemesterClicked = {},
                onEditCourseClicked = {},
                onDeleteCourseClicked = {},
                onDeleteSemesterClicked = {}
            )
        }
    }
}

@Preview(name = "Semesters Empty Light", showBackground = true)
@Preview(
    name = "Semesters Empty Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SemestersContentEmptyPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SemestersContent(
                uiState = SemestersUiState.Success(),
                onNavigateToCourseGrades = {},
                onAddCourseClicked = {},
                onEditSemesterClicked = {},
                onEditCourseClicked = {},
                onDeleteCourseClicked = {},
                onDeleteSemesterClicked = {}
            )
        }
    }
}

private fun previewSemesters(): List<SemesterWithCourse> = listOf(
    SemesterWithCourse(
        semester = Semester(
            id = "semester-2026-1",
            name = "Semestre 2026-1",
            startDate = LocalDate.of(2026, 2, 2),
            endDate = LocalDate.of(2026, 6, 6),
            isActive = true
        ),
        courses = listOf(
            previewCourse(
                id = "databases",
                name = "Bases de Datos",
                professor = "Ing. Laura Restrepo",
                room = "Bloque B · 302"
            ),
            previewCourse(
                id = "software-quality",
                name = "Calidad de Software",
                professor = "Mg. Andrés Molina",
                isRemote = true
            ),
            previewCourse(
                id = "networks",
                name = "Redes de Computadores",
                professor = "PhD. Camila Duarte",
                room = "Laboratorio 4"
            )
        )
    ),
    SemesterWithCourse(
        semester = Semester(
            id = "semester-2026-2",
            name = "Semestre 2026-2",
            startDate = LocalDate.of(2026, 8, 3),
            endDate = LocalDate.of(2026, 12, 5),
            isActive = false
        ),
        courses = emptyList()
    )
)
