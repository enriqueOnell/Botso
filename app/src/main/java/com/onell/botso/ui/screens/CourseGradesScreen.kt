package com.onell.botso.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.dialogs.course.CircularGradeProgress
import com.onell.botso.ui.components.dialogs.course.GradeInputSection
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.uistate.CourseGradesUiEvent
import com.onell.botso.ui.viewmodel.CourseGradesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseGradesScreen(
    courseId: Long,
    viewModel: CourseGradesViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    // 1. Disparamos el evento para establecer la materia inicial
    LaunchedEffect(courseId) {
        viewModel.onEvent(CourseGradesUiEvent.OnSetCourseId(courseId))
    }

    // 2. Observamos un ÚNICO estado para dominar toda la pantalla
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tabs = listOf("Corte 1", "Corte 2", "Corte 3")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas del Curso", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Regresar")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            CircularGradeProgress(
                score = uiState.averageScore,
                termAverage = uiState.currentTermAverage
            )

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryTabRow(
                selectedTabIndex = uiState.currentTermId - 1,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.currentTermId == index + 1,
                        // 3. Enviamos eventos al ViewModel en lugar de invocar funciones
                        onClick = { viewModel.onEvent(CourseGradesUiEvent.OnSetTermId(index + 1)) },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GradeInputSection(
                termId = uiState.currentTermId,
                formativa = uiState.stagedFormativa,
                cognitiva = uiState.stagedCognitiva,
                onFormativaChange = { viewModel.onEvent(CourseGradesUiEvent.OnUpdateFormativa(it)) },
                onCognitivaChange = { viewModel.onEvent(CourseGradesUiEvent.OnUpdateCognitiva(it)) },
                onSave = { viewModel.onEvent(CourseGradesUiEvent.OnSave) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun CourseGradesScreenPreview() {
    UniAppTheme {
        CourseGradesScreen(courseId = 1L)
    }
}