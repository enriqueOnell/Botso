package com.onell.botso.ui.components.tasks

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.theme.LocalBotsoColors

/**
 * Badge compacto con el estado de una tarea.
 *
 * Usa la semántica de Botso: neutral para pendientes, primario para lo que está
 * en curso y la pareja verde de aprobado para lo completado.
 */
@Composable
fun TaskStatusBadge(
    status: TaskStatus,
    modifier: Modifier = Modifier,
    containerColor: Color? = null,
    contentColor: Color? = null,
) {
    val semanticColors = LocalBotsoColors.current
    val (defaultContainerColor, defaultContentColor) = when (status) {
        TaskStatus.TODO -> MaterialTheme.colorScheme.surfaceContainerHighest to
            MaterialTheme.colorScheme.onSurfaceVariant
        TaskStatus.IN_PROGRESS -> MaterialTheme.colorScheme.primaryContainer to
            MaterialTheme.colorScheme.onPrimaryContainer
        TaskStatus.DONE -> semanticColors.passContainer to semanticColors.onPassContainer
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = containerColor ?: defaultContainerColor,
        contentColor = contentColor ?: defaultContentColor
    ) {
        Text(
            text = status.label,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Preview(name = "TaskStatusBadge Light", showBackground = true)
@Preview(
    name = "TaskStatusBadge Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TaskStatusBadgePreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TaskStatus.entries.forEach { status ->
                    TaskStatusBadge(status = status)
                }
            }
        }
    }
}
