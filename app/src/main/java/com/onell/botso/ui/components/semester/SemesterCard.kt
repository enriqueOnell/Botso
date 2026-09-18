package com.onell.botso.ui.components.semester

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import java.time.LocalDate
import java.time.LocalTime

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
    var expanded by remember { mutableStateOf(false) }
    var showSemesterMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { expanded = !expanded },
                    onLongPress = { showSemesterMenu = true }
                )
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = semesterData.semester.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )

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
                        leadingIcon = {
                            Icon(
                                Icons.Rounded.PictureAsPdf,
                                contentDescription = null
                            )
                        }
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


            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,

                ) {
                    Text(
                        text = "Materias",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onAddCourse) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Añadir Materia",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                semesterData.courses.forEach { courseWithSession ->
                    CourseRow(
                        courseWithSession = courseWithSession,
                        onClick = { onCourseClick(courseWithSession.course.id) },
                        onEdit = {
                            onEditCourse(courseWithSession)
                        },
                        onDelete = { onDeleteCourse(courseWithSession.course) }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun SemesterCardPreview() {
    SemesterCard(
        semesterData = SemesterWithCourse(
            semester = Semester(
                id = "1",
                name = "Semester 1",
                startDate = LocalDate.of(2002, 11, 2),
                endDate = LocalDate.of(2002, 11, 28)
            ),
            courses = listOf(
                CourseWithSession(
                    session = ClassSession(
                        "12",
                        "id2",
                        1,
                        LocalTime.of(10, 9),
                        LocalTime.of(20, 17),
                        "room",
                        true
                    ),
                    course = Course(
                        id = "id2",
                        semesterId = "semesterId",
                        name = "name",
                        professor = "professor"
                    )
                )
            )
        ),
        onCourseClick = {},
        onAddCourse = {},
        onEditSemester = {},
        onEditCourse = {},
        onDeleteCourse = {},
        onDeleteSemester = {}
    )
}
