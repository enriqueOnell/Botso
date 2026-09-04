package com.onell.botso.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Course // Modelos puros
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.Task
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskDialog(
    courses: List<Course>,
    onDismiss: () -> Unit,
    onConfirm: (Long, String, Long, Boolean, Int, String) -> Unit,
    modifier: Modifier = Modifier,
    task: Task? = null // Corregido: Debe ser Task? para aceptar null
) {
    var title by remember { mutableStateOf(task?.title ?: "") }
    var selectedCourseId by remember { mutableLongStateOf(task?.courseId ?: courses.firstOrNull()?.id ?: 0L) }
    var isPriority by remember { mutableStateOf(task?.isPriority ?: false) }
    var week by remember { mutableIntStateOf(task?.week ?: 1) }
    var description by remember { mutableStateOf(task?.description ?: "") }
    var expandedCourse by remember { mutableStateOf(false) }
    var expandedWeek by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = task?.dueDate ?: System.currentTimeMillis()
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
                        value = courses.find { it.id == selectedCourseId }?.name ?: "Seleccionar Curso",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCourse) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
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
                                Icon(Icons.Rounded.CalendarToday, contentDescription = "Seleccionar fecha")
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
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
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
                    if (title.isNotBlank() && selectedCourseId != 0L) {
                        onConfirm(
                            selectedCourseId,
                            title,
                            datePickerState.selectedDateMillis ?: System.currentTimeMillis(),
                            isPriority,
                            week,
                            description
                        )
                    }
                },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCourseDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Int, String, String, String, String, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    course: Course? = null // Corregido: Course?
) {
    var name by remember { mutableStateOf(course?.name ?: "") }
    var dayOfWeek by remember { mutableIntStateOf(course?.dayOfWeek ?: 1) }
    var startTime by remember { mutableStateOf(course?.startTime ?: "08:00") }
    var endTime by remember { mutableStateOf(course?.endTime ?: "10:00") }
    var professor by remember { mutableStateOf(course?.professor ?: "") }
    var location by remember { mutableStateOf(course?.location ?: "") }
    var isRemote by remember { mutableStateOf(course?.isRemote ?: false) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var expandedDay by remember { mutableStateOf(false) }
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text(if (course == null) "Nuevo Curso" else "Editar Curso") },
        text = {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Curso") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                ExposedDropdownMenuBox(
                    expanded = expandedDay,
                    onExpandedChange = { expandedDay = !expandedDay }
                ) {
                    OutlinedTextField(
                        value = days[dayOfWeek - 1],
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Día de Clase") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDay,
                        onDismissRequest = { expandedDay = false }
                    ) {
                        days.forEachIndexed { index, day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    dayOfWeek = index + 1
                                    expandedDay = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Inicio") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showStartTimePicker = true }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fin") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showEndTimePicker = true }
                        )
                    }
                }

                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Ubicación / Link") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Clase Remota")
                    Switch(checked = isRemote, onCheckedChange = { isRemote = it })
                }

                OutlinedTextField(
                    value = professor,
                    onValueChange = { professor = it },
                    label = { Text("Profesor") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(name, dayOfWeek, startTime, endTime, professor, location, isRemote)
                    }
                },
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(if (course == null) "Añadir" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )

    if (showStartTimePicker) {
        val parts = startTime.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        UniTimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                showStartTimePicker = false
            },
            initialHour = h,
            initialMinute = m
        )
    }

    if (showEndTimePicker) {
        val parts = endTime.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 10
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        UniTimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                showEndTimePicker = false
            },
            initialHour = h,
            initialMinute = m
        )
    }
}

@Composable
fun ConfirmDeleteDialog(
    title: String,
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text(title) },
        text = { Text(message) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Eliminar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSemesterDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    semester: Semester? = null // Corregido: Semester?
) {
    var name by remember { mutableStateOf(semester?.name ?: "") }

    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = semester?.startDate ?: System.currentTimeMillis())
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = semester?.endDate ?: (System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 4))

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text(if (semester == null) "Nuevo Semestre" else "Editar Semestre") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Semestre (ej. 2026-II)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                OutlinedTextField(
                    value = startDatePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha Inicio") },
                    trailingIcon = {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(Icons.Rounded.CalendarToday, contentDescription = "Seleccionar fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )

                OutlinedTextField(
                    value = endDatePickerState.selectedDateMillis?.let { millis ->
                        Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.of("UTC"))
                            .toLocalDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    } ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Fecha Fin") },
                    trailingIcon = {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(Icons.Rounded.CalendarToday, contentDescription = "Seleccionar fecha")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onConfirm(
                            name,
                            startDatePickerState.selectedDateMillis ?: System.currentTimeMillis(),
                            endDatePickerState.selectedDateMillis ?: System.currentTimeMillis()
                        )
                    }
                },
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(if (semester == null) "Crear" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )

    if (showStartDatePicker) {
        DatePickerDialog(onDismissRequest = { showStartDatePicker = false }, confirmButton = { TextButton(onClick = { showStartDatePicker = false }) { Text("OK") } }) {
            DatePicker(state = startDatePickerState)
        }
    }
    if (showEndDatePicker) {
        DatePickerDialog(onDismissRequest = { showEndDatePicker = false }, confirmButton = { TextButton(onClick = { showEndDatePicker = false }) { Text("OK") } }) {
            DatePicker(state = endDatePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditClassSessionDialog(
    courses: List<Course>, // Corregido: Course puro en lugar de CourseEntity
    onDismiss: () -> Unit,
    onConfirm: (Long, Int, String, String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCourseId by remember { mutableLongStateOf(courses.firstOrNull()?.id ?: 0L) }
    var dayOfWeek by remember { mutableIntStateOf(1) }
    var startTime by remember { mutableStateOf("08:00") }
    var endTime by remember { mutableStateOf("10:00") }
    var room by remember { mutableStateOf("") }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var expandedCourse by remember { mutableStateOf(false) }
    var expandedDay by remember { mutableStateOf(false) }

    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text("Nueva Sesión de Clase") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedCourse,
                    onExpandedChange = { expandedCourse = !expandedCourse }
                ) {
                    OutlinedTextField(
                        value = courses.find { it.id == selectedCourseId }?.name ?: "Seleccionar Curso",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Curso") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCourse) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
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

                ExposedDropdownMenuBox(
                    expanded = expandedDay,
                    onExpandedChange = { expandedDay = !expandedDay }
                ) {
                    OutlinedTextField(
                        value = days[dayOfWeek - 1],
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Día") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDay) },
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable, true).fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = expandedDay,
                        onDismissRequest = { expandedDay = false }
                    ) {
                        days.forEachIndexed { index, day ->
                            DropdownMenuItem(
                                text = { Text(day) },
                                onClick = {
                                    dayOfWeek = index + 1
                                    expandedDay = false
                                }
                            )
                        }
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Inicio") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showStartTimePicker = true }
                        )
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = endTime,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Fin") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp)
                        )
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .clickable { showEndTimePicker = true }
                        )
                    }
                }

                OutlinedTextField(
                    value = room,
                    onValueChange = { room = it },
                    label = { Text("Salón") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedCourseId != 0L) {
                        onConfirm(selectedCourseId, dayOfWeek, startTime, endTime, room)
                    }
                },
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Añadir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )

    if (showStartTimePicker) {
        val parts = startTime.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 8
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        UniTimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                showStartTimePicker = false
            },
            initialHour = h,
            initialMinute = m
        )
    }

    if (showEndTimePicker) {
        val parts = endTime.split(":")
        val h = parts.getOrNull(0)?.toIntOrNull() ?: 10
        val m = parts.getOrNull(1)?.toIntOrNull() ?: 0
        UniTimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
                showEndTimePicker = false
            },
            initialHour = h,
            initialMinute = m
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UniTimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    initialHour: Int = 8,
    initialMinute: Int = 0
) {
    val state = rememberTimePickerState(initialHour = initialHour, initialMinute = initialMinute)

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        confirmButton = {
            TextButton(onClick = { onConfirm(state.hour, state.minute) }, shape = RoundedCornerShape(24.dp)) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TimePicker(state = state)
            }
        }
    )
}