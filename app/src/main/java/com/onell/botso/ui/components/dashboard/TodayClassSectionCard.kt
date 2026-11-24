package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalTime

@Composable
fun TodayClassSectionCard(
    todayClasses: List<CourseWithSession>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Clases del Día",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                todayClasses.forEach { todayClass ->
                    key(todayClass.session.id) {
                        ClassItem(
                            sessionWithCourse = todayClass
                        )
                    }

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
                "room",
                true
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            )
        ),
        CourseWithSession(
            session = ClassSession(
                "13",
                "24",
                1,
                LocalTime.of(13, 0),
                LocalTime.of(15, 0),
                "room",
                true
            ),
            course = Course(
                id = "24",
                semesterId = "semesterId",
                name = "name",
                professor = "professor"
            )
        )
    )

    BotsoTheme {
        TodayClassSectionCard(
            todayClasses = todayClasses,
        )
    }
}