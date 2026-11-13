package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import java.lang.String.format
import java.util.Locale

/**
 * Fila resumen de una materia con su promedio.
 *
 * El color del indicador depende de si el promedio aprueba (>= 3.0).
 */
@Composable
fun CourseGradeItem(
    courseWithGrades: CourseWithGrades,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = courseWithGrades.course.name,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleLarge,
                )
                if (courseWithGrades.course.professor.isNotBlank()) {
                    Text(
                        text = courseWithGrades.course.professor,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomEnd){
                Text(
                    text = format(Locale.US, "%.2f", courseWithGrades.averageGrade),
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.displayMedium,
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CourseGradeItemPreview() {
    BotsoTheme {
        CourseGradeItem(
            courseWithGrades = CourseWithGrades(
                course = Course(
                    id = "id2",
                    semesterId = "semesterId",
                    name = "Estructuras de Datos",
                    professor = "Profesor Salcedo"
                ),
                grades = listOf(
                    Grade(
                        id = "g1",
                        courseId = "id2",
                        name = "Formativa",
                        score = 5.0,
                        weight = 1.0,
                        termId = 1
                    )
                )
            )
        )
    }
}
