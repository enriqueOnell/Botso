package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall,
            )

            if (courses.isEmpty()) {
                SectionEmptyState(
                    title = "Sin materias registradas",
                    subtitle = "Inscribe materias en la sección de Semestres",
                    icon = Icons.Rounded.School
                )
            } else {
                HorizontalUncontainedCarousel(
                    state = rememberCarouselState { courses.size },
                    modifier = Modifier.fillMaxWidth(),
                    itemWidth = 180.dp,
                    itemSpacing = 12.dp,
                    contentPadding = PaddingValues(horizontal = 4.dp)
                ) { index ->
                    val courseWithGrades = courses[index]

                    CourseGradeItem(
                        courseWithGrades = courseWithGrades,
                        modifier = Modifier.maskClip(MaterialTheme.shapes.extraLarge)
                    )
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
        )
    )
    BotsoTheme {
        CourseGradeSectionCard(courses)
    }
}

@Preview(name = "CourseGradeSectionCardEmpty")
@Composable
private fun PreviewCourseGradeSectionCardEmpty() {
    BotsoTheme {
        CourseGradeSectionCard(emptyList())
    }
}
