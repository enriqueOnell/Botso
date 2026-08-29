package com.onell.botso.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.AddEditCourseDialog
import com.onell.botso.ui.components.ConfirmDeleteDialog
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.viewmodel.ScheduleEntry
import com.onell.botso.ui.viewmodel.ScheduleUiEvent
import com.onell.botso.ui.viewmodel.ScheduleViewModel
import com.onell.botso.ui.viewmodel.SemestersUiEvent
import com.onell.botso.ui.viewmodel.SemestersViewModel
import java.time.LocalTime

@Composable
fun ScheduleScreen(
    viewModel: ScheduleViewModel = hiltViewModel(),
    semestersViewModel: SemestersViewModel = hiltViewModel() // Used for editing courses
) {
    val entries by viewModel.scheduleEntries.collectAsStateWithLifecycle()
    
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
        if (entry.session != null && entry.course != null) {
            // Edit course (or session? Requirement says "Editar" on class card)
            // If it's a session, maybe we should have an EditSessionDialog?
            // For now, I'll allow editing the course associated with it.
            AddEditCourseDialog(
                course = entry.course,
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
        } else if (entry.course != null) {
            AddEditCourseDialog(
                course = entry.course,
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

@Composable
fun ScheduleTable(
    entries: List<ScheduleEntry>,
    onEdit: (ScheduleEntry) -> Unit,
    onDelete: (ScheduleEntry) -> Unit,
    modifier: Modifier = Modifier
) {
    val days = listOf("LUNES", "MARTES", "MIÉRCOLES", "JUEVES", "VIERNES", "SÁBADO", "DOMINGO")
    val startHour = 6
    val endHour = 22
    val hourHeight = 90.dp
    val dayWidth = 140.dp
    val hourColumnWidth = 70.dp
    val headerHeight = 40.dp

    val horizontalScrollState = rememberScrollState()
    val verticalScrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .horizontalScroll(horizontalScrollState)
            .verticalScroll(verticalScrollState)
    ) {
        Column {
            Row {
                Box(
                    modifier = Modifier
                        .size(width = hourColumnWidth, height = headerHeight)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .border(0.5.dp, Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "HORAS",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                days.forEach { day ->
                    Box(
                        modifier = Modifier
                            .size(width = dayWidth, height = headerHeight)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .border(0.5.dp, Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            (startHour..endHour).forEach { hour ->
                Row {
                    Box(
                        modifier = Modifier
                            .size(width = hourColumnWidth, height = hourHeight)
                            .background(Color.White)
                            .border(0.5.dp, Color.LightGray),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = String.format("%02d:00", hour),
                            modifier = Modifier.padding(top = 8.dp),
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.Gray
                        )
                    }
                    repeat(7) {
                        Box(
                            modifier = Modifier
                                .size(width = dayWidth, height = hourHeight)
                                .background(Color.White)
                                .border(0.5.dp, Color.LightGray)
                        )
                    }
                }
            }
        }

        entries.forEach { entry ->
            val dayIndex = entry.dayOfWeek - 1
            
            if (dayIndex in 0..6) {
                val startTime = try { LocalTime.parse(entry.startTime) } catch (e: Exception) { null }
                val endTime = try { LocalTime.parse(entry.endTime) } catch (e: Exception) { null }

                if (startTime != null && endTime != null) {
                    val startTotalMinutes = startTime.hour * 60 + startTime.minute
                    val baseTotalMinutes = startHour * 60
                    
                    val topOffsetMinutes = startTotalMinutes - baseTotalMinutes
                    val durationMinutes = java.time.Duration.between(startTime, endTime).toMinutes()
                    
                    if (durationMinutes <= 0) return@forEach // Fix potential "Fatality" bug

                    val topOffsetDp = (topOffsetMinutes.toFloat() / 60f * hourHeight.value).dp + headerHeight
                    val heightDp = (durationMinutes.toFloat() / 60f * hourHeight.value).dp
                    val leftOffsetDp = hourColumnWidth + (dayWidth * dayIndex)

                    var showMenu by remember { mutableStateOf(false) }

                    Box(
                        modifier = Modifier
                            .offset(x = leftOffsetDp, y = topOffsetDp)
                            .size(width = dayWidth, height = heightDp)
                            .padding(1.dp)
                    ) {
                        ScheduleSessionCard(
                            courseName = entry.name,
                            timeRange = "${entry.startTime} - ${entry.endTime}",
                            location = entry.location,
                            colorHex = entry.colorHex,
                            modifier = Modifier
                                .fillMaxSize()
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onLongPress = { showMenu = true }
                                    )
                                }
                        )

                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Editar") },
                                onClick = {
                                    onEdit(entry)
                                    showMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Eliminar") },
                                onClick = {
                                    onDelete(entry)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ScheduleSessionCard(
    courseName: String,
    timeRange: String,
    location: String,
    colorHex: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor = try {
        Color(android.graphics.Color.parseColor(colorHex))
    } catch (e: Exception) {
        MaterialTheme.colorScheme.primaryContainer
    }.copy(alpha = 0.9f)

    val contentColor = if (isColorDark(backgroundColor)) Color.White else Color.Black

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        ),
        border = BorderStroke(0.5.dp, Color.Black.copy(alpha = 0.1f))
    ) {
        Column(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = courseName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )
            Text(
                text = timeRange,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (location.isNotEmpty()) {
                Text(
                    text = location,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun isColorDark(color: Color): Boolean {
    val darkness = 1 - (0.299 * color.red + 0.587 * color.green + 0.114 * color.blue)
    return darkness >= 0.5
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun ScheduleTablePreview() {
    UniAppTheme {
        ScheduleTable(entries = emptyList(), onEdit = {}, onDelete = {})
    }
}
