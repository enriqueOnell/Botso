package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Fila de tarea prioritaria — Material 3 Expressive.
 * Muestra el título, la materia (en lugar de la descripción) y la fecha formateada (día, número y mes).
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PriorityTaskItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    val dayFormatter = remember {
        DateTimeFormatter.ofPattern("EEE", Locale.forLanguageTag("es"))

    }

    val dayNumberFormatter = remember {
        DateTimeFormatter.ofPattern("d", Locale.forLanguageTag("es"))
    }

    val monthFormatter = remember {
        DateTimeFormatter.ofPattern("MMM", Locale.forLanguageTag("es"))
    }

    val day = task.dueDate.format(dayFormatter)
    val dayNumber = task.dueDate.format(dayNumberFormatter)
    val month = task.dueDate.format(monthFormatter)

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Surface(
            color = MaterialTheme.colorScheme.errorContainer,
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = day,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
                Text(
                    text = dayNumber,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error,
                )
                Text(
                    text = month,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        Spacer(modifier = Modifier.padding(8.dp))
        Column {
            Text(
                text = task.title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.padding(2.dp))
            Text(
                text = task.description,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }

}

@Preview(showBackground = true)
@Composable
private fun PriorityTaskItemPreview() {
    BotsoTheme {
        PriorityTaskItem(
            task = Task(
                id = "id",
                courseId = "courseId",
                title = "Entrega de proyecto final",
                dueDate = LocalDate.now(),
                isPriority = true,
                hasAttachment = true,
                status = TaskStatus.TODO,
                week = 3,
                description = "Descripción oculta"
            ),
        )
    }
}
