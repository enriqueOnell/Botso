package com.onell.UniApp.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.onell.UniApp.ui.theme.UniAppTheme
import com.onell.UniApp.ui.viewmodel.CourseGradesViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseGradesScreen(
    courseId: Long,
    viewModel: CourseGradesViewModel = hiltViewModel(),
    onBack: () -> Unit = {}
) {
    LaunchedEffect(courseId) {
        viewModel.setCourseId(courseId)
    }

    val grades by viewModel.grades.collectAsState()
    val averageScore by viewModel.averageScore.collectAsState()
    val currentTermAverage by viewModel.currentTermAverage.collectAsState()
    val currentTermId by viewModel.currentTermId.collectAsState()
    val stagedFormativa by viewModel.stagedFormativa.collectAsState()
    val stagedCognitiva by viewModel.stagedCognitiva.collectAsState()

    val tabs = listOf("Corte 1", "Corte 2", "Corte 3")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas del Curso", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
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
            
            CircularGradeProgress(score = averageScore, termAverage = currentTermAverage)
            
            Spacer(modifier = Modifier.height(32.dp))

            PrimaryTabRow(
                selectedTabIndex = currentTermId - 1,
                containerColor = Color.Transparent,
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = currentTermId == index + 1,
                        onClick = { viewModel.setTermId(index + 1) },
                        text = { Text(title) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            GradeInputSection(
                termId = currentTermId,
                formativa = stagedFormativa,
                cognitiva = stagedCognitiva,
                onFormativaChange = { viewModel.updateStagedFormativa(it) },
                onCognitivaChange = { viewModel.updateStagedCognitiva(it) },
                onSave = { viewModel.saveGrades() }
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

@Composable
fun CircularGradeProgress(score: Double, termAverage: Double) {
    val animatedScore by animateFloatAsState(targetValue = score.toFloat(), label = "score")
    val progress = (animatedScore / 5.0f).coerceIn(0f, 1f)
    
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(200.dp)) {
        CircularProgressIndicator(
            progress = { progress },
            modifier = Modifier.fillMaxSize(),
            color = if (score >= 3.0) Color(0xFFA0F399) else MaterialTheme.colorScheme.error,
            strokeWidth = 12.dp,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            strokeCap = StrokeCap.Round
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = String.format(Locale.US, "%.1f", score),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Promedio Total",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Surface(
                color = if (score >= 3.0) Color(0xFFA0F399).copy(alpha = 0.2f) else MaterialTheme.colorScheme.errorContainer,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = String.format(Locale.US, "Corte: %.1f", termAverage),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (score >= 3.0) Color(0xFF002106) else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
fun GradeInputSection(
    termId: Int,
    formativa: String,
    cognitiva: String,
    onFormativaChange: (String) -> Unit,
    onCognitivaChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val percentage = if (termId == 3) "20%" else "15%"
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            GradeInputField(
                label = "Nota Formativa ($percentage)",
                value = formativa,
                onValueChange = onFormativaChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            GradeInputField(
                label = "Nota Cognitiva ($percentage)",
                value = cognitiva,
                onValueChange = onCognitivaChange
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Guardar Notas")
            }
        }
    }
}


@Composable
fun GradeInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            singleLine = true,
            placeholder = { Text("0.0") }
        )
    }
}
