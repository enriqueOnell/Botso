package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalTime

/**
 * Tarjeta de una clase del día.
 *
 * Muestra horario, aula o modalidad remota, nombre de la materia y profesor.
 * Es un componente puramente visual.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ClassItem(sessionWithCourse: CourseWithSession, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {

            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-20).dp, y = 0.dp)
                    .size(150.dp)
                    .clip(MaterialShapes.Square.toShape())
                    .background(MaterialTheme.colorScheme.primary)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth().padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text(
                        text = "${sessionWithCourse.session.startTime}",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primaryContainer
                    )

                    Text(
                        text = "${sessionWithCourse.session.endTime}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primaryContainer,
                    )

                }

                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = sessionWithCourse.course.name,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row {
                        Icon(
                            Icons.Rounded.Person,
                            contentDescription = "profesor",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = sessionWithCourse.course.professor,
                            style = MaterialTheme.typography.bodyMedium,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                    Row {
                        Icon(
                            Icons.Rounded.LocationOn,
                            contentDescription = "location",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (sessionWithCourse.session.isRemote) "Remota" else sessionWithCourse.session.room,
                            style = MaterialTheme.typography.bodyMedium,

                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassItemRemotePreview() {
    BotsoTheme {
        val todayClasses = CourseWithSession(
            session = ClassSession(
                "12",
                "id2",
                1,
                LocalTime.of(10, 9),
                LocalTime.of(20, 17),
                "room",
                true
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "Materia 1",
                professor = "Profesor"
            )
        )

        ClassItem(sessionWithCourse = todayClasses)
    }
}

@Preview(showBackground = true)
@Composable
private fun ClassItemWhitRoomPreview() {
    BotsoTheme {
        val todayClasses = CourseWithSession(
            session = ClassSession(
                "12",
                "id2",
                1,
                LocalTime.of(10, 9),
                LocalTime.of(20, 17),
                "salon 404 - 20 de julio",
                false
            ),
            course = Course(
                id = "id2",
                semesterId = "semesterId",
                name = "Materia 1",
                professor = "Profesor"
            )
        )

        ClassItem(sessionWithCourse = todayClasses)
    }
}