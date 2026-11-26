package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.EventAvailable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayClassSectionCard(
    todayClasses: List<CourseWithSession>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Clases del Día",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (todayClasses.isEmpty()) {
                SectionEmptyState(
                    title = "¡Día libre!",
                    subtitle = "No tienes clases programadas para hoy",
                    icon = Icons.Rounded.EventAvailable
                )
            } else {
                HorizontalUncontainedCarousel(
                    state = rememberCarouselState { todayClasses.size },
                    modifier = Modifier.fillMaxWidth(),
                    itemWidth = 280.dp,
                    itemSpacing = 12.dp,
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) { index ->
                    val todayClass = todayClasses[index]

                    ClassItem(
                        sessionWithCourse = todayClass,
                        modifier = Modifier.maskClip(MaterialTheme.shapes.large)
                    )
                }
            }
        }
    }
}

@Preview(name = "TodayClassSectionCard")
@Composable
private fun PreviewTodayClassSectionCard() {
    val todayClasses = listOf(
        CourseWithSession(
            session = ClassSession(
                "12",
                "id2",
                1,
                LocalTime.of(10, 0),
                LocalTime.of(12, 0),
                "Salón 302",
                true
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "Sistemas Operativos",
                professor = "Edgar Morillo"
            )
        ),
        CourseWithSession(
            session = ClassSession(
                "13",
                "id3",
                1,
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                "Lab 4",
                false
            ),
            course = Course(
                id = "id3",
                semesterId = "semesterId",
                name = "Calidad de Software",
                professor = "Luis Bedoya"
            )
        )
    )

    BotsoTheme {
        TodayClassSectionCard(
            todayClasses = todayClasses,
        )
    }
}

@Preview(name = "TodayClassSectionCardEmpty")
@Composable
private fun PreviewTodayClassSectionCardEmpty() {
    BotsoTheme {
        TodayClassSectionCard(
            todayClasses = emptyList(),
        )
    }
}