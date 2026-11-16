package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
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
fun PriorityItem(
    task: Task,
    modifier: Modifier = Modifier
) {
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("EEE, d 'de' MMM", Locale.forLanguageTag("es"))
    }
    val formattedDate = try {
        task.dueDate.format(dateFormatter).replaceFirstChar { it.uppercase() }
    } catch (_: Exception) {
        task.dueDate.toString()
    }

    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Surface(
                shape = MaterialShapes.Cookie6Sided.toShape(),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = Icons.Default.PriorityHigh,
                    contentDescription = "Browser",
                    tint = MaterialTheme.colorScheme.primaryContainer
                )
            }

            Spacer(modifier = Modifier.padding(8.dp))
            Column {
                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PriorityItemPreview() {
    BotsoTheme {
        PriorityItem(
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
