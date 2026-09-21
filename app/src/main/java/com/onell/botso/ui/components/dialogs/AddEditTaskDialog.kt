package com.onell.botso.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenu
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Task
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    courses: List<Course>,
    onDismiss: () -> Unit,
    onConfirm: (String, String, LocalDate, Boolean, Int, String) -> Unit,
    modifier: Modifier = Modifier,
    task: Task? = null
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var selectedCourseId by remember {
        mutableStateOf(
            task?.courseId ?: ""
        )
    }

    LaunchedEffect(courses) {
        if (selectedCourseId.isBlank() && courses.isNotEmpty()) {
            selectedCourseId = courses.first().id
        }
    }
    var isPriority by remember { mutableStateOf(task?.isPriority ?: false) }
    var week by remember { mutableIntStateOf(task?.week ?: 1) }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var expandedCourse by remember { mutableStateOf(false) }
    var expandedWeek by remember { mutableStateOf(false) }

    val initialDateMillis = task?.dueDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli() ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis
    )
    var showDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text(if (task == null) "Nueva Tarea" else "Editar Tarea") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Título de la tarea") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedCourse,
                    onExpandedChange = { expandedCourse = !expandedCourse }
                ) {
                    OutlinedTextField(
                        value = courses.find { it.id == selectedCourseId }?.name
                            ?: "Seleccionar Curso",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCourse) },
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedCourse,
                        onDismissRequest = { expandedCourse = false }
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course.name) },
                                onClick = {
                                    selectedCourseId = course.id
                                    expandedCourse = false
                                }
                            )
                        }
                    }
                }
                if (courses.isEmpty()) {
                    Text(
                        text = "⚠️ No hay cursos disponibles. Debes crear un curso primero.",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = datePickerState.selectedDateMillis?.let { millis ->
                            Instant.ofEpochMilli(millis)
                                .atZone(ZoneId.of("UTC"))
                                .toLocalDate()
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                        } ?: "",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Fecha") },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    Icons.Rounded.CalendarToday,
                                    contentDescription = "Seleccionar fecha"
                                )
                            }
                        },
                        modifier = Modifier.weight(1.5f),
                        shape = RoundedCornerShape(24.dp)
                    )

                    ExposedDropdownMenuBox(
                        expanded = expandedWeek,
                        onExpandedChange = { expandedWeek = !expandedWeek },
                        modifier = Modifier.weight(1f)
                    ) {
                        OutlinedTextField(
                            value = "Semana $week",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Semana") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedWeek) },
                            modifier = Modifier
                                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = expandedWeek,
                            onDismissRequest = { expandedWeek = false }
                        ) {
                            (1..16).forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("Semana $w") },
                                    onClick = {
                                        week = w
                                        expandedWeek = false
                                    }
                                )
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    shape = RoundedCornerShape(24.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isPriority, onCheckedChange = { isPriority = it })
                    Text("Marcar como prioridad")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && selectedCourseId.isNotBlank()) {
                        val dueDateMillis = datePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        val dueDate = Instant.ofEpochMilli(dueDateMillis).atZone(ZoneId.of("UTC")).toLocalDate()
                        onConfirm(
                            selectedCourseId,
                            title,
                            dueDate,
                            isPriority,
                            week,
                            description
                        )
                    }
                },
                enabled = title.isNotBlank() && selectedCourseId.isNotBlank() && courses.isNotEmpty(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(if (task == null) "Añadir" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
