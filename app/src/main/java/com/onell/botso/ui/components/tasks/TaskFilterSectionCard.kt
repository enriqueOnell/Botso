package com.onell.botso.ui.components.tasks

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Sección de filtros de Tareas.
 *
 * Agrupa los estados en un contenedor neutro con botones segmentados
 * tokenizados. El contador corresponde a las tareas visibles con el filtro
 * activo; no conoce el ViewModel ni la lista completa.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TaskFilterSectionCard(
    selectedStatus: TaskStatus,
    taskCount: Int,
    onStatusSelected: (TaskStatus) -> Unit,
    modifier: Modifier = Modifier,
) {
    val statuses = TaskStatus.entries

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Filtrar tareas",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Toca una tarea para avanzar de estado.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Text(
                        text = taskCount.toString(),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                statuses.forEachIndexed { index, status ->
                    SegmentedButton(
                        modifier = Modifier.weight(1f),
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = statuses.size
                        ),
                        onClick = { onStatusSelected(status) },
                        selected = status == selectedStatus,
                        label = {
                            Text(
                                text = status.label,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = MaterialTheme.colorScheme.primary,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }
        }
    }
}

@Preview(name = "TaskFilterSectionCard Light", showBackground = true)
@Preview(
    name = "TaskFilterSectionCard Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TaskFilterSectionCardPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            TaskFilterSectionCard(
                selectedStatus = TaskStatus.TODO,
                taskCount = 3,
                onStatusSelected = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
