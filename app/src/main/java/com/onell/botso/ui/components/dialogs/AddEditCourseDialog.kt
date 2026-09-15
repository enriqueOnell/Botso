package com.onell.botso.ui.components.dialogs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCourseDialog(
    modifier: Modifier = Modifier,
    semesterId: String = "",
    onDismiss: () -> Unit,
    onConfirm: (Course, ClassSession) -> Unit,
    sessionWithCourse: ClassSessionWithCourse? = null
) {
    var courseId by remember { mutableStateOf(sessionWithCourse?.course?.id ?: "") }
    var sessionId by remember { mutableStateOf(sessionWithCourse?.session?.id ?: "") }
    var currentSemesterId by remember { mutableStateOf(sessionWithCourse?.course?.semesterId?.takeIf { it.isNotBlank() } ?: semesterId) }
    var name by remember { mutableStateOf(sessionWithCourse?.course?.name ?: "") }
    var dayOfWeek by remember { mutableIntStateOf(sessionWithCourse?.session?.dayOfWeek ?: 1) }
    var startTime by remember { mutableStateOf(sessionWithCourse?.session?.startTime ?: LocalTime.of(8, 0)) }
    var endTime by remember { mutableStateOf(sessionWithCourse?.session?.endTime ?: LocalTime.of(10, 0)) }
    var professor by remember { mutableStateOf(sessionWithCourse?.course?.professor ?: "") }
    var location by remember { mutableStateOf(sessionWithCourse?.session?.room ?: "") }
    var isRemote by remember { mutableStateOf(sessionWithCourse?.session?.isRemote ?: false) }

    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    var expandedDay by remember { mutableStateOf(false) }
    val days = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        title = { Text(if (sessionWithCourse?.course == null) "Nuevo Curso" else "Editar Curso") },
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
                            value = startTime.format(DateTimeFormatter.ofPattern("HH:mm")),
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
                            value = endTime.format(DateTimeFormatter.ofPattern("HH:mm")),
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
                        val newCourse = Course(
                            id = courseId,
                            semesterId = currentSemesterId,
                            name = name,
                            professor = professor
                        )
                        val newSession = ClassSession(
                            id = sessionId,
                            courseId = courseId,
                            dayOfWeek = dayOfWeek,
                            startTime = startTime,
                            endTime = endTime,
                            room = location,
                            isRemote = isRemote
                        )
                        onConfirm(newCourse, newSession)
                    }
                },
                enabled = name.isNotBlank(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text(if (sessionWithCourse?.course == null) "Añadir" else "Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, shape = RoundedCornerShape(24.dp)) {
                Text("Cancelar")
            }
        }
    )

    if (showStartTimePicker) {
        BotsoTimePickerDialog(
            onDismiss = { showStartTimePicker = false },
            onConfirm = { hour, minute ->
                startTime = LocalTime.of(hour, minute)
                showStartTimePicker = false
            },
            initialHour = startTime.hour,
            initialMinute = startTime.minute
        )
    }

    if (showEndTimePicker) {
        BotsoTimePickerDialog(
            onDismiss = { showEndTimePicker = false },
            onConfirm = { hour, minute ->
                endTime = LocalTime.of(hour, minute)
                showEndTimePicker = false
            },
            initialHour = endTime.hour,
            initialMinute = endTime.minute
        )
    }
}
