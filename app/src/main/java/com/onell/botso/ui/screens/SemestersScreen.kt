package com.onell.botso.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.Course // Modelos puros del dominio
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats
import com.onell.botso.ui.components.AddEditCourseDialog
import com.onell.botso.ui.components.AddEditSemesterDialog
import com.onell.botso.ui.components.ConfirmDeleteDialog
import com.onell.botso.ui.theme.UniAppTheme
import com.onell.botso.ui.uistate.SemestersUiEvent
import com.onell.botso.ui.viewmodel.SemestersViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemestersScreen(
    viewModel: SemestersViewModel = hiltViewModel(),
    onNavigateToCourseGrades: (Long) -> Unit = {}
) {
    // 1. Observamos el estado centralizado purgado
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val semestersWithStats = uiState.semestersWithStats

    var showAddSemesterDialog by remember { mutableStateOf(false) }
    var showAddCourseDialogForSemesterId by remember { mutableStateOf<Long?>(null) }

    // 2. Erradicamos los "Entity" de las variables de estado
    var semesterToEdit by remember { mutableStateOf<Semester?>(null) }
    var courseToEdit by remember { mutableStateOf<Course?>(null) }
    var courseToDelete by remember { mutableStateOf<Course?>(null) }
    var semesterToDelete by remember { mutableStateOf<Semester?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión Académica", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { showAddSemesterDialog = true },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(24.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar Semestre")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(semestersWithStats) { stats ->
                SemesterCard(
                    stats = stats,
                    onCourseClick = onNavigateToCourseGrades,
                    onAddCourse = { showAddCourseDialogForSemesterId = stats.semester.id },
                    onEditSemester = { semesterToEdit = it },
                    onEditCourse = { courseToEdit = it },
                    onDeleteCourse = { courseToDelete = it },
                    onDeleteSemester = { semesterToDelete = it }
                )
            }
        }
    }

    if (showAddSemesterDialog) {
        AddEditSemesterDialog(
            onDismiss = { showAddSemesterDialog = false },
            onConfirm = { name, start, end ->
                viewModel.onEvent(SemestersUiEvent.OnAddSemester(name, start, end, true))
                showAddSemesterDialog = false
            }
        )
    }

    semesterToEdit?.let { semester ->
        AddEditSemesterDialog(
            semester = semester,
            onDismiss = { semesterToEdit = null },
            onConfirm = { name, start, end ->
                viewModel.onEvent(SemestersUiEvent.OnUpdateSemester(semester, name, start, end, semester.isActive))
                semesterToEdit = null
            }
        )
    }

    showAddCourseDialogForSemesterId?.let { semesterId ->
        AddEditCourseDialog(
            onDismiss = { showAddCourseDialogForSemesterId = null },
            onConfirm = { name, dayOfWeek, start, end, professor, location, isRemote ->
                viewModel.onEvent(SemestersUiEvent.OnAddCourse(semesterId, name, dayOfWeek, start, end, professor, "#4f378a", location, isRemote))
                showAddCourseDialogForSemesterId = null
            }
        )
    }

    courseToEdit?.let { course ->
        AddEditCourseDialog(
            course = course,
            onDismiss = { courseToEdit = null },
            onConfirm = { name, dayOfWeek, start, end, professor, location, isRemote ->
                viewModel.onEvent(SemestersUiEvent.OnEditCourse(course, name, dayOfWeek, start, end, professor, course.colorHex, location, isRemote))
                courseToEdit = null
            }
        )
    }

    courseToDelete?.let { course ->
        ConfirmDeleteDialog(
            title = "Eliminar Curso",
            message = "¿Estás seguro de que deseas eliminar el curso '${course.name}'? Esta acción no se puede deshacer.",
            onConfirm = {
                viewModel.onEvent(SemestersUiEvent.OnDeleteCourseConfirm(course))
                courseToDelete = null
            },
            onDismiss = { courseToDelete = null }
        )
    }

    semesterToDelete?.let { semester ->
        ConfirmDeleteDialog(
            title = "Eliminar Semestre",
            message = "¿Estás seguro de que deseas eliminar el semestre '${semester.name}'? Se eliminarán todos los cursos y notas asociados.",
            onConfirm = {
                viewModel.onEvent(SemestersUiEvent.OnDeleteSemester(semester))
                semesterToDelete = null
            },
            onDismiss = { semesterToDelete = null }
        )
    }
}

@Composable
fun SemesterCard(
    stats: SemesterWithStats,
    onCourseClick: (Long) -> Unit,
    onAddCourse: () -> Unit,
    // 3. Los callbacks exigen modelos puros
    onEditSemester: (Semester) -> Unit,
    onEditCourse: (Course) -> Unit,
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
                stats.courses.forEach { course ->
                    CourseRow(
                        course = course,
                        onClick = { onCourseClick(course.id) },
                        onEdit = { onEditCourse(course) },
                        onDelete = { onDeleteCourse(course) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CourseRow(
    course: Course, // 4. Adiós CourseEntity
    onClick: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = { showMenu = true }
                ),
            color = Color.Transparent,
            shape = RoundedCornerShape(24.dp)
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp, horizontal = 12.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = course.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Ver notas >",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        DropdownMenu(
            expanded = showMenu,
            onDismissRequest = { showMenu = false }
        ) {
            DropdownMenuItem(
                text = { Text("Editar Curso") },
                onClick = {
                    onEdit()
                    showMenu = false
                },
                leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Eliminar Curso") },
                onClick = {
                    onDelete()
                    showMenu = false
                },
                leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null) },
                colors = MenuDefaults.itemColors(
                    textColor = MaterialTheme.colorScheme.error,
                    leadingIconColor = MaterialTheme.colorScheme.error
                )
            )
        }
    }
}

@Composable
fun StatusChip(isActive: Boolean) {
    Surface(
        color = if (isActive) Color(0xFFA0F399) else Color.LightGray,
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = if (isActive) "Activo" else "Archivado",
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = if (isActive) Color(0xFF002106) else Color.DarkGray
        )
    }
}

@Composable
fun StatItem(label: String, value: String, isHighlight: Boolean = false) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = if (isHighlight) Color(0xFF4CAF50) else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Composable
private fun SemestersScreenPreview() {
    UniAppTheme {
        SemestersScreen()
    }
}
