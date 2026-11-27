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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PriorityTaskItem(
    task: Task,
    modifier: Modifier = Modifier,
    courseName: String = "",
) {
    val localeEs = remember { Locale.forLanguageTag("es") }
    val dayOfWeekFormatter = remember { DateTimeFormatter.ofPattern("EEE", localeEs) }
    val dayNumberFormatter = remember { DateTimeFormatter.ofPattern("d", localeEs) }
    val monthFormatter = remember { DateTimeFormatter.ofPattern("MMM", localeEs) }

    val dayOfWeek = task.dueDate.format(dayOfWeekFormatter).replace(".", "").uppercase()
    val dayNumber = task.dueDate.format(dayNumberFormatter)
    val month = task.dueDate.format(monthFormatter).replace(".", "").lowercase()

    val displayCourseName = courseName.ifBlank {
        if (task.courseId.isNotBlank()) "Materia ${task.courseId}" else "Materia"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            contentColor = MaterialTheme.colorScheme.onTertiaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Surface(
                        shape = MaterialTheme.shapes.extraSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                                contentDescription = "Materia",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.tertiaryContainer
                            )
                            Text(
                                text = displayCourseName,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.tertiaryContainer,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    if (task.hasAttachment) {
                        Icon(
                            imageVector = Icons.Rounded.AttachFile,
                            contentDescription = "Tiene archivo adjunto",
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Surface(
                modifier = Modifier.widthIn(min = 64.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Text(
                        text = dayNumber,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                    Text(
                        text = month,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PriorityTaskItemPreview() {
    BotsoTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            PriorityTaskItem(
                task = Task(
                    id = "id1",
                    courseId = "58",
                    title = "Taller de Redes OAuth2",
                    dueDate = LocalDate.of(2026, 11, 27),
                    isPriority = true,
                    hasAttachment = true,
                    status = TaskStatus.TODO,
                    week = 14,
                    description = "Subir captura de Wireshark y repositorio antes de medianoche."
                ),
                courseName = "Redes de Computadores"
            )
            PriorityTaskItem(
                task = Task(
                    id = "id2",
                    courseId = "59",
                    title = "Parcial Calidad de Software",
                    dueDate = LocalDate.of(2026, 12, 2),
                    isPriority = true,
                    hasAttachment = false,
                    status = TaskStatus.TODO,
                    week = 15,
                    description = ""
                ),
                courseName = "Calidad de Software"
            )
        }
    }
}
