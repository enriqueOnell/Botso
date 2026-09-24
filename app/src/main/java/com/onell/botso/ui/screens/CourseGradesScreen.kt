package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.courseGrade.CircularGradeProgress
import com.onell.botso.ui.components.courseGrade.GradeInputSection
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.CourseGradesUiState
import com.onell.botso.ui.viewmodel.CourseGradesViewModel

@Composable
fun CourseGradesScreen(
    courseId: String,
    viewModel: CourseGradesViewModel = hiltViewModel(),
    onCourseLoaded: (String) -> Unit = {},
    onNavigateBack: () -> Unit = {}
) {
    LaunchedEffect(courseId) {
        viewModel.loadCourseData(courseId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState) {
        if (uiState is CourseGradesUiState.Success) {
            val courseName = (uiState as CourseGradesUiState.Success).courseData?.course?.name
            if (!courseName.isNullOrEmpty()) {
                onCourseLoaded(courseName)
            }
        }
    }

    CourseGradesContent(
        uiState = uiState,
        onTermSelected = viewModel::setTermId,
        onFormativaChange = viewModel::updateStagedFormativa,
        onCognitivaChange = viewModel::updateStagedCognitiva,
        onSave = viewModel::saveGrades,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CourseGradesContent(
    uiState: CourseGradesUiState,
    onTermSelected: (Int) -> Unit,
    onFormativaChange: (String) -> Unit,
    onCognitivaChange: (String) -> Unit,
    onSave: () -> Unit,
    onNavigateBack: () -> Unit
) {
    val tabs = listOf("Corte 1", "Corte 2", "Corte 3")

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .weight(1f)) {
            when (uiState) {
                is CourseGradesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingIndicator()
                    }
                }

                is CourseGradesUiState.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                is CourseGradesUiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        val totalAverage = uiState.courseData?.averageGrade ?: 0.0

                        CircularGradeProgress(
                            score = totalAverage,
                            termAverage = uiState.currentTermAverage
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        PrimaryTabRow(
                            selectedTabIndex = uiState.currentTermId - 1,
                            containerColor = Color.Transparent,
                            divider = {}
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    selected = uiState.currentTermId == index + 1,
                                    onClick = { onTermSelected(index + 1) },
                                    text = { Text(title) }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        GradeInputSection(
                            termId = uiState.currentTermId,
                            formativa = uiState.stagedFormativa,
                            cognitiva = uiState.stagedCognitiva,
                            onFormativaChange = onFormativaChange,
                            onCognitivaChange = onCognitivaChange,
                            onSave = onSave
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CourseGradesScreenPreview() {
    BotsoTheme {
        CourseGradesContent(
            uiState = CourseGradesUiState.Success(
                courseData = null,
                currentTermId = 1,
                stagedFormativa = "4.5",
                stagedCognitiva = "3.8",
                currentTermAverage = 4.15
            ),
            onTermSelected = {},
            onFormativaChange = {},
            onCognitivaChange = {},
            onSave = {},
            onNavigateBack = {}
        )
    }
}