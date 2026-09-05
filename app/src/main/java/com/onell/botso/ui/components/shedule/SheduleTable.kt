package com.onell.botso.ui.components.shedule

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.screens.ScheduleSessionCard
import com.onell.botso.ui.uistate.ScheduleEntry
import java.time.LocalTime
import kotlin.collections.forEach

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