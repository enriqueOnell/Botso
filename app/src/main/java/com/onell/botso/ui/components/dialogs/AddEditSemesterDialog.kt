package com.onell.botso.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Semester
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditSemesterDialog(
    onDismiss: () -> Unit,
    onConfirm: (Semester) -> Unit, // ¡FIRMA CORREGIDA! Exige el modelo puro
    modifier: Modifier = Modifier,
    semester: Semester? = null
) {
    var name by remember { mutableStateOf(semester?.name ?: "") }

    val startDateMillis = semester?.startDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli() ?: System.currentTimeMillis()
    val endDateMillis = semester?.endDate?.atStartOfDay(ZoneId.of("UTC"))?.toInstant()?.toEpochMilli() ?: (System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30 * 4)

    val startDatePickerState = rememberDatePickerState(initialSelectedDateMillis = startDateMillis)
    val endDatePickerState = rememberDatePickerState(initialSelectedDateMillis = endDateMillis)

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val sheetState = rememberBottomSheetState(
        initialValue = SheetValue.Hidden,
        enabledValues = setOf(SheetValue.Hidden, SheetValue.Expanded)
    )
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(top = 8.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = if (semester == null) "Nuevo Semestre" else "Editar Semestre",
                style = MaterialTheme.typography.titleLarge
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre del Semestre") },
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

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TextButton(
                    onClick = {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onDismiss()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text("Cancelar")
                }

                Button(
                    onClick = {
                        if (name.isNotBlank()) {
                            val startMillis = startDatePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            val finalStartDate = Instant.ofEpochMilli(startMillis).atZone(ZoneId.of("UTC")).toLocalDate()

                            val endMillis = endDatePickerState.selectedDateMillis ?: System.currentTimeMillis()
                            val finalEndDate = Instant.ofEpochMilli(endMillis).atZone(ZoneId.of("UTC")).toLocalDate()

                            val resultSemester = Semester(
                                id = semester?.id ?: UUID.randomUUID().toString(),
                                name = name,
                                startDate = finalStartDate,
                                endDate = finalEndDate,
                                isActive = semester?.isActive ?: true
                            )

                            onConfirm(resultSemester) // ¡AQUÍ ENTREGAMOS EL OBJETO!
                        }
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(if (semester == null) "Crear" else "Guardar")
                }
            }
        }
    }

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
