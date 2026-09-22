package com.onell.botso.ui.components.semester

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SemesterCard(
    semesterData: SemesterWithCourse,
    onCourseClick: (String) -> Unit,
    onAddCourse: () -> Unit,
    onEditSemester: (Semester) -> Unit,
    onEditCourse: (CourseWithSession) -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onDeleteSemester: (Semester) -> Unit,
    onExportPdf: (Semester) -> Unit = {}
) {
    var showSemesterMenu by remember { mutableStateOf(false) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = { showSemesterMenu = true }
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = semesterData.semester.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${semesterData.semester.startDate.format(dateFormatter)} - ${semesterData.semester.endDate.format(dateFormatter)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                IconButton(
                    onClick = onAddCourse,
                    colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Añadir Materia",
                        modifier = Modifier.size(28.dp)
                    )
                }

                DropdownMenu(
                    expanded = showSemesterMenu,
                    onDismissRequest = { showSemesterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar Semestre") },
                        onClick = {
                            onEditSemester(semesterData.semester)
                            showSemesterMenu = false
                        },
                        leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Exportar a PDF") },
                        onClick = {
                            onExportPdf(semesterData.semester)
                            showSemesterMenu = false
                        },
                        leadingIcon = { Icon(Icons.Rounded.PictureAsPdf, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar Semestre") },
                        onClick = {
                            onDeleteSemester(semesterData.semester)
                            showSemesterMenu = false
                        },
                        leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null) },
                        colors = MenuDefaults.itemColors(
                            textColor = MaterialTheme.colorScheme.error,
                            leadingIconColor = MaterialTheme.colorScheme.error
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (semesterData.courses.isEmpty()) {
                Text(
                    text = "Aún no hay materias registradas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            } else {
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    semesterData.courses.forEach { courseWithSession ->
                        CourseRow(
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