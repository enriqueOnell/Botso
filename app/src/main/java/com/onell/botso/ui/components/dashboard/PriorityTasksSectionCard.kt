package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalDate

@Composable
fun PriorityTasksSectionCard(
    priorityTasks: List<Task>,
    modifier: Modifier = Modifier,
    coursesMap: Map<String, String> = emptyMap()
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLow),
        shape = MaterialTheme.shapes.large
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tareas con Prioridad",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            if (priorityTasks.isEmpty()) {
                SectionEmptyState(
                    title = "¡Todo al día!",
                    subtitle = "No tienes tareas prioritarias pendientes",
                    icon = Icons.Rounded.TaskAlt
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    priorityTasks.forEach { task ->
                        key(task.id) {
                            PriorityTaskItem(
                                task = task,
                                courseName = coursesMap[task.courseId] ?: ""
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "PriorityTasksSectionCard")
@Composable
private fun PreviewPriorityTasksSectionCard() {
    val priorityTasks = listOf(
        Task(
            id = "id",
            courseId = "58",
            title = "Tarea 1",
            dueDate = LocalDate.now(),
            isPriority = true,
            hasAttachment = true,
            status = TaskStatus.TODO,
            week = 3,
            description = "Hay que hacer tarea 1"
        )
    )

    BotsoTheme {
        PriorityTasksSectionCard(
            priorityTasks = priorityTasks,
            coursesMap = mapOf("58" to "Estructuras de Datos")
        )
    }
}

@Preview(name = "PriorityTasksSectionCardEmpty")
@Composable
private fun PreviewPriorityTasksSectionCardEmpty() {
    BotsoTheme {
        PriorityTasksSectionCard(emptyList())
    }
}
