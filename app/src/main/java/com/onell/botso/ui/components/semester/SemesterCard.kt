package com.onell.botso.ui.components.semester

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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats
import java.util.Locale

@Composable
fun SemesterCard(
    stats: SemesterWithStats,
    onCourseClick: (String) -> Unit,
    onAddCourse: () -> Unit,
    // 3. Los callbacks exigen modelos puros
    onEditSemester: (Semester) -> Unit,
    onEditCourse: (ClassSessionWithCourse) -> Unit,
    onDeleteCourse: (Course) -> Unit,
    onDeleteSemester: (Semester) -> Unit
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
                    text = stats.semester.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                StatusChip(isActive = stats.semester.isActive)

                DropdownMenu(
                    expanded = showSemesterMenu,
                    onDismissRequest = { showSemesterMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Editar Semestre") },
                        onClick = {
                            onEditSemester(stats.semester)
                            showSemesterMenu = false
                        },
                        leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
                    )
                    DropdownMenuItem(
                        text = { Text("Eliminar Semestre") },
                        onClick = {
                            onDeleteSemester(stats.semester)
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(label = "Cursos", value = stats.courseCount.toString())
                StatItem(
                    label = "Promedio Est.",
                    value = String.format(Locale.US, "%.2f", stats.estimatedGpa),
                    isHighlight = stats.estimatedGpa >= 3.0
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Cursos", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                    IconButton(onClick = onAddCourse) {
                        Icon(Icons.Default.Add, contentDescription = "Añadir Curso", modifier = Modifier.size(20.dp))
                    }
                }
                stats.courses.forEach { sessionWithCourse ->
                    CourseRow(
                        sessionWithCourse = sessionWithCourse,
                        onClick = { onCourseClick(sessionWithCourse.course.id) },
                        onEdit = { onEditCourse(sessionWithCourse) },
                        onDelete = { onDeleteCourse(sessionWithCourse.course) }
                    )
                }
            }
        }
    }
}