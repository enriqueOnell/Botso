package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import com.onell.botso.ui.theme.BotsoTheme

@Composable
fun CourseGradeSectionCard(
    courses: List<CourseWithGrades>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Mis materias",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                courses.chunked(2).forEach { rowCourses ->
                    key(rowCourses.first().course.id) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (courseWithGrades in rowCourses) {
                                key(courseWithGrades.course.id) {
                                    CourseGradeItem(
                                        courseWithGrades = courseWithGrades,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxHeight()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "CourseGradeSectionCard")
@Composable
private fun PreviewCourseGradeSectionCard() {
    val courses = listOf(
        CourseWithGrades(
            course = Course(
                id = "id1",
                semesterId = "semesterId",
                name = "Ecuaciones Diferenciales",
                professor = "Luis Bedoya"
            ),
            grades = listOf(
                Grade(
                    id = "g1",
                    courseId = "id1",
                    name = "Corte 1",
                    score = 4.2,
                    weight = 0.3,
                    termId = 1
                )
            )
        ),
        CourseWithGrades(
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "Sistemas Operativos",
                professor = "Edgar Morillo"
            ),
            grades = listOf(
                Grade(
                    id = "g2",
                    courseId = "id2",
                    name = "Corte 1",
                    score = 3.8,
                    weight = 0.3,
                    termId = 1
                )
            )
        ),
        CourseWithGrades(
            course = Course(
                id = "id3",
                semesterId = "semesterId",
                name = "Auditoría Informática",
                professor = "Profesor"
            ),
            grades = emptyList()
        )
    )
    BotsoTheme {
        CourseGradeSectionCard(courses)
    }
}