package com.onell.botso.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Semester
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSemesterDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, Long, Long) -> Unit,
    modifier: Modifier = Modifier,
    semester: Semester? = null // Corregido: Semester?
) {
    var id by remember { mutableStateOf(semester?.id ?: "") }
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
                            id,
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