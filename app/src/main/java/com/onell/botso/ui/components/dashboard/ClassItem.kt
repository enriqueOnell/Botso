package com.onell.botso.ui.components.dashboard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Computer
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * Tarjeta de una clase del día.
 * Presenta el horario en jerarquía tipográfica grande arriba, seguido del nombre
 * de la materia y dos filas apiladas de ancho completo para profesor y ubicación/modalidad.
 */
@Composable
fun ClassItem(
    sessionWithCourse: CourseWithSession,
    modifier: Modifier = Modifier
) {
    val isRemote = sessionWithCourse.session.isRemote
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = sessionWithCourse.session.startTime.format(timeFormatter),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Row(
                    modifier = Modifier.padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "–",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = sessionWithCourse.session.endTime.format(timeFormatter),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = sessionWithCourse.course.name,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                ClassInfoRowPill(
                    icon = Icons.Outlined.Person,
                    contentDescription = "Profesor",
                    text = sessionWithCourse.course.professor
                )

                ClassInfoRowPill(
                    icon = if (isRemote) Icons.Outlined.Computer else Icons.Outlined.LocationOn,
                    contentDescription = "Ubicación",
                    text = if (isRemote) "Remota" else sessionWithCourse.session.room
                )
            }
        }
    }
}

@Composable
private fun ClassInfoRowPill(
    icon: ImageVector,
    contentDescription: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ClassItemPreview() {
    BotsoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ClassItem(
                sessionWithCourse = CourseWithSession(
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
                        name = "Sistemas Operativos",
                        professor = "Edgar Morillo"
                    )
                )
            )
            ClassItem(
                sessionWithCourse = CourseWithSession(
                    session = ClassSession(
                        "13",
                        "id3",
                        1,
                        LocalTime.of(14, 0),
                        LocalTime.of(16, 30),
                        "Salón 404 - 20 de Julio",
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
        }
    }
}