package com.onell.botso.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.Grade
import com.onell.botso.ui.components.grades.CourseSummarySectionCard
import com.onell.botso.ui.components.grades.GradeEditorSectionCard
import com.onell.botso.ui.components.grades.GradesSectionCard
import com.onell.botso.ui.components.grades.previewCourseWithGrades
import com.onell.botso.ui.components.grades.previewFailingGrades
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.CourseGradesUiState
import com.onell.botso.ui.viewmodel.CourseGradesViewModel
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

/**
 * Pantalla de Notas de un curso.
 *
 * Muestra el resumen del curso, las notas del corte seleccionado y el editor
 * numérico. Toda la validación y el guardado se delegan a `CourseGradesViewModel`.
 */
@Composable
fun CourseGradesScreen(
    courseId: String,
    viewModel: CourseGradesViewModel = hiltViewModel(),
    onCourseLoad: (String) -> Unit = {},
) {
    LaunchedEffect(courseId) {
        viewModel.loadCourseData(courseId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState, onCourseLoad) {
        if (uiState is CourseGradesUiState.Success) {
            val courseName = (uiState as CourseGradesUiState.Success).courseData?.course?.name
            if (!courseName.isNullOrEmpty()) {
                onCourseLoad(courseName)
            }
        }
    }

    CourseGradesContent(
        uiState = uiState,
        onTermSelected = viewModel::setTermId,
        onFormativaChange = viewModel::updateStagedFormativa,
        onCognitivaChange = viewModel::updateStagedCognitiva,
        onSave = viewModel::saveGrades,
        onEditGrade = viewModel::editGrade,
        onDeleteGrade = viewModel::deleteGrade
    )
}

/**
 * Contenido puro de Notas.
 *
 * No conoce el ViewModel: recibe el estado y los callbacks. La raíz queda
 * transparente y la lista reserva espacio inferior para la toolbar flotante.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CourseGradesContent(
    uiState: CourseGradesUiState,
    onTermSelected: (Int) -> Unit,
    onFormativaChange: (String) -> Unit,
    onCognitivaChange: (String) -> Unit,
    onSave: () -> Unit,
    onEditGrade: (Grade) -> Unit,
    onDeleteGrade: (Grade) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is CourseGradesUiState.Loading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is CourseGradesUiState.Error -> {
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

        is CourseGradesUiState.Success -> {
            val courseData = uiState.courseData
            val allGrades = courseData?.grades ?: emptyList()
            val termGrades = remember(allGrades, uiState.currentTermId) {
                allGrades.filter { it.termId == uiState.currentTermId }
            }
            val evaluatedPercent = remember(allGrades) {
                (allGrades.sumOf { it.weight } * 100).roundToInt().coerceIn(0, 100)
            }
            val listState = rememberLazyListState()
            val scope = rememberCoroutineScope()
            var activeField by remember { mutableStateOf("formativa") }

            LazyColumn(
                state = listState,
                modifier = modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 96.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item(key = "course-summary") {
                    CourseSummarySectionCard(
                        courseName = courseData?.course?.name ?: "Materia",
                        average = courseData?.averageGrade ?: 0.0,
                        evaluatedPercent = evaluatedPercent,
                        termId = uiState.currentTermId,
                        termAverage = uiState.currentTermAverage
                    )
                }

                item(key = "grades") {
                    GradesSectionCard(
                        termId = uiState.currentTermId,
                        grades = termGrades,
                        hasAnyGrades = allGrades.isNotEmpty(),
                        onTermSelected = onTermSelected,
                        onEditGrade = { grade ->
                            activeField = if (grade.name.contains("Cognitiva")) {
                                "cognitiva"
                            } else {
                                "formativa"
                            }
                            onEditGrade(grade)
                            scope.launch { listState.animateScrollToItem(2) }
                        },
                        onDeleteGrade = onDeleteGrade
                    )
                }

                item(key = "grade-editor") {
                    GradeEditorSectionCard(
                        termId = uiState.currentTermId,
                        formativa = uiState.stagedFormativa,
                        cognitiva = uiState.stagedCognitiva,
                        onFormativaChange = onFormativaChange,
                        onCognitivaChange = onCognitivaChange,
                        onSave = onSave,
                        activeField = activeField,
                        onActiveFieldChange = { activeField = it }
                    )
                }
            }
        }
    }
}

@Preview(name = "CourseGrades Passing Light", showBackground = true)
@Preview(
    name = "CourseGrades Passing Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CourseGradesPassingPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CourseGradesContent(
                uiState = CourseGradesUiState.Success(
                    courseData = previewCourseWithGrades(),
                    currentTermId = 1,
                    stagedFormativa = "4.5",
                    stagedCognitiva = "4.0",
                    currentTermAverage = 4.25
                ),
                onTermSelected = {},
                onFormativaChange = {},
                onCognitivaChange = {},
                onSave = {},
                onEditGrade = {},
                onDeleteGrade = {}
            )
        }
    }
}

@Preview(name = "CourseGrades Failing Light", showBackground = true)
@Preview(
    name = "CourseGrades Failing Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CourseGradesFailingPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CourseGradesContent(
                uiState = CourseGradesUiState.Success(
                    courseData = previewCourseWithGrades(
                        courseName = "Cálculo Diferencial",
                        grades = previewFailingGrades()
                    ),
                    currentTermId = 2,
                    stagedFormativa = "1.8",
                    stagedCognitiva = "2.2",
                    currentTermAverage = 2.0
                ),
                onTermSelected = {},
                onFormativaChange = {},
                onCognitivaChange = {},
                onSave = {},
                onEditGrade = {},
                onDeleteGrade = {}
            )
        }
    }
}

@Preview(name = "CourseGrades Empty Light", showBackground = true)
@Preview(
    name = "CourseGrades Empty Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CourseGradesEmptyPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            CourseGradesContent(
                uiState = CourseGradesUiState.Success(
                    courseData = previewCourseWithGrades(grades = emptyList()),
                    currentTermId = 1,
                    stagedFormativa = "",
                    stagedCognitiva = "",
                    currentTermAverage = 0.0
                ),
                onTermSelected = {},
                onFormativaChange = {},
                onCognitivaChange = {},
                onSave = {},
                onEditGrade = {},
                onDeleteGrade = {}
            )
        }
    }
}
