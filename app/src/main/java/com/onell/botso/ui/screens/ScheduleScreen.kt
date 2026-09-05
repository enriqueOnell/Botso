package com.onell.botso.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.dialogs.AddEditCourseDialog
import com.onell.botso.ui.components.dialogs.ConfirmDeleteDialog
import com.onell.botso.ui.components.shedule.ScheduleTable
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.uistate.ScheduleEntry
import com.onell.botso.ui.uistate.ScheduleUiEvent
import com.onell.botso.ui.uistate.SemestersUiEvent
import com.onell.botso.ui.viewmodel.ScheduleViewModel
import com.onell.botso.ui.viewmodel.SemestersViewModel

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    semestersViewModel: SemestersViewModel = hiltViewModel() // Se usa para disparar la edición del curso
) {
    // 1. Observamos el estado centralizado purgado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val entries = uiState.scheduleEntries

    var entryToEdit by remember { mutableStateOf<ScheduleEntry?>(null) }
    var entryToDelete by remember { mutableStateOf<ScheduleEntry?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Text(
            text = "Mi Horario",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        ScheduleTable(
            entries = entries,
            onEdit = { entryToEdit = it },
            onDelete = { entryToDelete = it },
            modifier = Modifier.fillMaxSize()
        )
    }

    entryToEdit?.let { entry ->
        if (entry.course != null) {
            AddEditCourseDialog(
                course = entry.course, // Asegúrate de que este Dialog ahora espere un 'Course' puro
                onDismiss = { entryToEdit = null },
                onConfirm = { name, dayOfWeek, start, end, professor, location, isRemote ->
                    semestersViewModel.onEvent(
                        SemestersUiEvent.OnEditCourse(
                            entry.course, name, dayOfWeek, start, end, professor, entry.course.colorHex, location, isRemote
                        )
                    )
                    entryToEdit = null
                }
            )
        }
    }

    entryToDelete?.let { entry ->
        ConfirmDeleteDialog(
            title = "Eliminar Clase",
            message = "¿Estás seguro de que deseas eliminar '${entry.name}'? Si es una sesión adicional se eliminará, si es la clase principal deberás eliminar el curso.",
            onConfirm = {
                // 2. Disparamos eventos al ViewModel limpiamente
                if (entry.session != null) {
                    viewModel.onEvent(ScheduleUiEvent.OnDeleteSession(entry.session))
                } else if (entry.course != null) {
                    viewModel.onEvent(ScheduleUiEvent.OnDeleteCourse(entry.course))
                }
                entryToDelete = null
            },
            onDismiss = { entryToDelete = null }
        )
    }
}




@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun ScheduleTablePreview() {
    UniAppTheme {
        ScheduleTable(entries = emptyList(), onEdit = {}, onDelete = {})
    }
}
