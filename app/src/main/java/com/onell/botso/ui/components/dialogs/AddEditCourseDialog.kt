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
import com.onell.botso.domain.model.Course
import java.util.Locale

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