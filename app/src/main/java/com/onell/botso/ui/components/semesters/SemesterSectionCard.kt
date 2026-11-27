package com.onell.botso.ui.components.semesters

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.ui.components.dashboard.SectionEmptyState
import com.onell.botso.ui.theme.BotsoTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Tarjeta de un semestre con el resumen del periodo y sus materias.
 *
 * El encabezado concentra el nombre, el rango de fechas, el estado y el número
 * de materias; las acciones (añadir materia y menú de semestre) viven a la
 * derecha. La pulsación larga también abre el menú del semestre.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SemesterSectionCard(
    semesterData: SemesterWithCourse,
    onCourseClick: (String) -> Unit,
    onAddCourse: () -> Unit,
    onEditSemester: (Semester) -> Unit,
    onEditCourse: (CourseWithSession) -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onDeleteSemester: (Semester) -> Unit,
    onExportPdf: (Semester) -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var showSemesterMenu by remember { mutableStateOf(false) }
    val semester = semesterData.semester

    val dateFormatter = remember {
        DateTimeFormatter.ofPattern("d MMM yyyy", Locale.forLanguageTag("es"))
    }
    val dateRange = remember(semester.startDate, semester.endDate) {
        "${semester.startDate.format(dateFormatter)} – ${semester.endDate.format(dateFormatter)}"
    }
    val courseCount = semesterData.courses.size
    val courseCountLabel = if (courseCount == 1) "1 materia" else "$courseCount materias"

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { showSemesterMenu = true },
                onLongClickLabel = "Acciones del semestre"
            ),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.Top) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = semester.name,
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = dateRange,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SemesterStateBadge(isActive = semester.isActive)

                        Surface(
                            shape = MaterialTheme.shapes.extraSmall,
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.MenuBook,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = courseCountLabel,
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    FilledTonalIconButton(
                        onClick = onAddCourse,
                        shape = MaterialTheme.shapes.medium,
                        colors = IconButtonDefaults.filledTonalIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Añadir materia"
                        )
                    }

                    Box {
                        IconButton(onClick = { showSemesterMenu = true }) {
                            Icon(
                                imageVector = Icons.Rounded.MoreVert,
                                contentDescription = "Acciones del semestre",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        DropdownMenu(
                            expanded = showSemesterMenu,
                            onDismissRequest = { showSemesterMenu = false },
                            shape = MaterialTheme.shapes.medium,
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar semestre") },
                                onClick = {
                                    showSemesterMenu = false
                                    onEditSemester(semester)
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.Edit, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Exportar a PDF") },
                                onClick = {
                                    showSemesterMenu = false
                                    onExportPdf(semester)
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.PictureAsPdf, contentDescription = null)
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar semestre") },
                                onClick = {
                                    showSemesterMenu = false
                                    onDeleteSemester(semester)
                                },
                                leadingIcon = {
                                    Icon(Icons.Rounded.Delete, contentDescription = null)
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

            if (semesterData.courses.isEmpty()) {
                SectionEmptyState(
                    title = "Sin materias en este semestre",
                    subtitle = "Añade tu primera materia con el botón + del semestre.",
                    icon = Icons.AutoMirrored.Rounded.MenuBook
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    semesterData.courses.forEach { courseWithSession ->
                        key(courseWithSession.course.id) {
                            SemesterCourseItem(
                                courseWithSession = courseWithSession,
                                onClick = { onCourseClick(courseWithSession.course.id) },
                                onEdit = { onEditCourse(courseWithSession) },
                                onDelete = { onDeleteCourse(courseWithSession.course) }
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Badge de estado del semestre: activo en menta, finalizado en neutro.
 */
@Composable
private fun SemesterStateBadge(
    isActive: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraSmall,
        color = if (isActive) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        contentColor = if (isActive) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    ) {
        Text(
            text = if (isActive) "Activo" else "Finalizado",
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1
        )
    }
}

@Preview(name = "SemesterSectionCard Light", showBackground = true)
@Preview(
    name = "SemesterSectionCard Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SemesterSectionCardPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SemesterSectionCard(
                semesterData = SemesterWithCourse(
                    semester = Semester(
                        id = "semester-2026-1",
                        name = "Semestre 2026-1",
                        startDate = LocalDate.of(2026, 2, 2),
                        endDate = LocalDate.of(2026, 6, 6),
                        isActive = true
                    ),
                    courses = listOf(
                        previewCourse(
                            id = "databases",
                            name = "Bases de Datos",
                            professor = "Ing. Laura Restrepo",
                            room = "Bloque B · 302"
                        ),
                        previewCourse(
                            id = "software-quality",
                            name = "Calidad de Software",
                            professor = "Mg. Andrés Molina",
                            isRemote = true
                        ),
                        previewCourse(
                            id = "networks",
                            name = "Redes de Computadores",
                            professor = "PhD. Camila Duarte",
                            room = "Laboratorio 4"
                        )
                    )
                ),
                onCourseClick = {},
                onAddCourse = {},
                onEditSemester = {},
                onEditCourse = {},
                onDeleteCourse = {},
                onDeleteSemester = {},
                onExportPdf = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "SemesterSectionCard Empty Light", showBackground = true)
@Preview(
    name = "SemesterSectionCard Empty Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SemesterSectionCardEmptyPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SemesterSectionCard(
                semesterData = SemesterWithCourse(
                    semester = Semester(
                        id = "semester-empty",
                        name = "Semestre 2026-2",
                        startDate = LocalDate.of(2026, 8, 3),
                        endDate = LocalDate.of(2026, 12, 5),
                        isActive = false
                    ),
                    courses = emptyList()
                ),
                onCourseClick = {},
                onAddCourse = {},
                onEditSemester = {},
                onEditCourse = {},
                onDeleteCourse = {},
                onDeleteSemester = {},
                onExportPdf = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
