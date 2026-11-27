package com.onell.botso.ui.components.tasks

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.Book
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.model.Task
import com.onell.botso.domain.model.TaskStatus
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.theme.LocalBotsoColors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Tarjeta individual de una tarea.
 *
 * Jerarquía: materia y estado, título, descripción y metadatos (fecha, semana,
 * prioridad y adjunto). Un toque avanza el estado; la pulsación larga abre el
 * menú de editar/eliminar. El componente es puramente visual y recibe los
 * callbacks desde la pantalla.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    courseWithTask: CourseWithTask,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val task = courseWithTask.task
    val course = courseWithTask.course
    var showMenu by remember { mutableStateOf(false) }

    val semanticColors = LocalBotsoColors.current
    val isDone = task.status == TaskStatus.DONE
    val isPriority = task.isPriority && !isDone

    val containerColor = when {
        isDone -> semanticColors.passContainer
        isPriority -> MaterialTheme.colorScheme.tertiaryContainer
        else -> MaterialTheme.colorScheme.surfaceContainer
    }
    val contentColor = when {
        isDone -> semanticColors.onPassContainer
        isPriority -> MaterialTheme.colorScheme.onTertiaryContainer
        else -> MaterialTheme.colorScheme.onSurface
    }
    val secondaryContentColor = contentColor.copy(alpha = 0.78f)
    val neutralBadgeContainer = contentColor.copy(alpha = 0.12f)

    val today = LocalDate.now()
    val isUrgent = !isDone && !task.dueDate.isAfter(today)
    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("EEE d MMM", Locale.forLanguageTag("es"))
    }
    val dueDateText = remember(task.dueDate) {
        task.dueDate.format(dateFormatter).replace(".", "")
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = { showMenu = true },
                onLongClickLabel = "Editar o eliminar tarea"
            ),
        shape = MaterialTheme.shapes.large,
        color = containerColor,
        contentColor = contentColor
    ) {
        Box {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f, fill = false),
                        shape = MaterialTheme.shapes.extraSmall,
                        color = neutralBadgeContainer,
                        contentColor = contentColor
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Book,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = contentColor
                            )
                            Text(
                                text = course.name.ifBlank { "Materia" },
                                style = MaterialTheme.typography.labelSmall,
                                color = contentColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    TaskStatusBadge(
                        status = task.status,
                        containerColor = if (isDone || isPriority) {
                            neutralBadgeContainer
                        } else {
                            null
                        },
                        contentColor = if (isDone || isPriority) contentColor else null
                    )
                }

                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                if (task.description.isNotBlank()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryContentColor,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    itemVerticalAlignment = Alignment.CenterVertically
                ) {
                    TaskMetaBadge(
                        icon = Icons.Rounded.CalendarToday,
                        text = dueDateText,
                        containerColor = if (isUrgent) {
                            MaterialTheme.colorScheme.errorContainer
                        } else {
                            neutralBadgeContainer
                        },
                        contentColor = if (isUrgent) {
                            MaterialTheme.colorScheme.onErrorContainer
                        } else {
                            contentColor
                        }
                    )

                    TaskMetaBadge(
                        icon = Icons.Rounded.DateRange,
                        text = "Semana ${task.week}",
                        containerColor = neutralBadgeContainer,
                        contentColor = contentColor
                    )

                    if (isPriority) {
                        TaskMetaBadge(
                            icon = Icons.Rounded.Star,
                            text = "Prioritaria",
                            containerColor = MaterialTheme.colorScheme.tertiary,
                            contentColor = MaterialTheme.colorScheme.onTertiary
                        )
                    }

                    if (task.hasAttachment) {
                        Icon(
                            imageVector = Icons.Rounded.AttachFile,
                            contentDescription = "Tiene archivo adjunto",
                            modifier = Modifier.size(18.dp),
                            tint = secondaryContentColor
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                shape = MaterialTheme.shapes.medium,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            ) {
                DropdownMenuItem(
                    text = { Text("Editar tarea") },
                    onClick = {
                        showMenu = false
                        onEdit()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = null
                        )
                    }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar tarea") },
                    onClick = {
                        showMenu = false
                        onDelete()
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = null
                        )
                    },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.error,
                        leadingIconColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }
    }
}

/**
 * Píldora compacta para los metadatos de la tarea.
 */
@Composable
private fun TaskMetaBadge(
    icon: ImageVector,
    text: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Preview(name = "TaskItem Light", showBackground = true)
@Preview(
    name = "TaskItem Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun TaskItemPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TaskItem(
                    courseWithTask = previewTask(
                        id = "todo",
                        title = "Taller de Redes OAuth2",
                        courseName = "Redes de Computadores",
                        status = TaskStatus.TODO,
                        week = 14,
                        dueDate = LocalDate.of(2026, 12, 2),
                        description = "Subir captura de Wireshark y el repositorio antes de medianoche.",
                        hasAttachment = true
                    ),
                    onClick = {},
                    onDelete = {},
                    onEdit = {}
                )

                TaskItem(
                    courseWithTask = previewTask(
                        id = "priority",
                        title = "Parcial Calidad de Software",
                        courseName = "Calidad de Software",
                        status = TaskStatus.IN_PROGRESS,
                        week = 15,
                        dueDate = LocalDate.of(2026, 11, 27),
                        description = "Repasar criterios de calidad y métricas del proyecto.",
                        isPriority = true
                    ),
                    onClick = {},
                    onDelete = {},
                    onEdit = {}
                )

                TaskItem(
                    courseWithTask = previewTask(
                        id = "done",
                        title = "Quiz de Bases de Datos",
                        courseName = "Bases de Datos",
                        status = TaskStatus.DONE,
                        week = 12,
                        dueDate = LocalDate.of(2026, 11, 20),
                        description = "Entregado en clase."
                    ),
                    onClick = {},
                    onDelete = {},
                    onEdit = {}
                )
            }
        }
    }
}

private fun previewTask(
    id: String,
    title: String,
    courseName: String,
    status: TaskStatus,
    week: Int,
    dueDate: LocalDate,
    description: String,
    isPriority: Boolean = false,
    hasAttachment: Boolean = false,
): CourseWithTask = CourseWithTask(
    task = Task(
        id = id,
        courseId = "course-$id",
        title = title,
        dueDate = dueDate,
        isPriority = isPriority,
        hasAttachment = hasAttachment,
        status = status,
        week = week,
        description = description
    ),
    course = Course(
        id = "course-$id",
        semesterId = "semester-preview",
        name = courseName,
        professor = "Profesor"
    )
)
