package com.onell.botso.ui.components.grades

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Assignment
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.domain.model.Grade
import com.onell.botso.ui.components.dashboard.SectionEmptyState
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Sección de cortes y notas.
 *
 * Permite cambiar entre los tres cortes y lista las calificaciones del corte
 * seleccionado. Si el corte aún no tiene notas, muestra `SectionEmptyState`.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GradesSectionCard(
    termId: Int,
    grades: List<Grade>,
    onTermSelected: (Int) -> Unit,
    onEditGrade: (Grade) -> Unit,
    onDeleteGrade: (Grade) -> Unit,
    modifier: Modifier = Modifier,
    hasAnyGrades: Boolean = grades.isNotEmpty(),
) {
    val terms = listOf(1, 2, 3)

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Cortes y notas",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                terms.forEachIndexed { index, term ->
                    SegmentedButton(
                        modifier = Modifier.weight(1f),
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = terms.size
                        ),
                        onClick = { onTermSelected(term) },
                        selected = term == termId,
                        label = { Text("Corte $term", maxLines = 1) },
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = MaterialTheme.colorScheme.primary,
                            activeContentColor = MaterialTheme.colorScheme.onPrimary,
                            activeBorderColor = MaterialTheme.colorScheme.primary,
                            inactiveContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                            inactiveContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            inactiveBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )
                }
            }

            if (grades.isEmpty()) {
                SectionEmptyState(
                    title = if (hasAnyGrades) "Sin notas en este corte" else "Sin notas registradas",
                    subtitle = if (hasAnyGrades) {
                        "Registra la nota Formativa y Cognitiva de este corte para ver tu avance."
                    } else {
                        "Registra tus primeras calificaciones con el teclado numérico de abajo."
                    },
                    icon = Icons.AutoMirrored.Rounded.Assignment
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    grades.forEach { grade ->
                        key(grade.id) {
                            GradeItem(
                                grade = grade,
                                onEdit = { onEditGrade(grade) },
                                onDelete = { onDeleteGrade(grade) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "GradesSection Light", showBackground = true)
@Preview(
    name = "GradesSection Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GradesSectionCardPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            GradesSectionCard(
                termId = 1,
                grades = previewPassingGrades().filter { it.termId == 1 },
                onTermSelected = {},
                onEditGrade = {},
                onDeleteGrade = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview(name = "GradesSection Empty Light", showBackground = true)
@Preview(
    name = "GradesSection Empty Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GradesSectionCardEmptyPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            GradesSectionCard(
                termId = 3,
                grades = emptyList(),
                onTermSelected = {},
                onEditGrade = {},
                onDeleteGrade = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
