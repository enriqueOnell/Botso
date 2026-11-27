package com.onell.botso.ui.components.grades

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Sección de edición de notas de un corte.
 *
 * Contiene los campos Formativa/Cognitiva, el peso del corte y el teclado
 * numérico propio. Toda la validación vive en el ViewModel.
 */
@Composable
fun GradeEditorSectionCard(
    termId: Int,
    formativa: String,
    cognitiva: String,
    onFormativaChange: (String) -> Unit,
    onCognitivaChange: (String) -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    activeField: String? = null,
    onActiveFieldChange: ((String) -> Unit)? = null,
) {
    val percentage = if (termId == 3) "20%" else "15%"
    var internalActiveField by remember { mutableStateOf("formativa") }
    val selectedField = activeField ?: internalActiveField
    val selectField: (String) -> Unit = { field ->
        internalActiveField = field
        onActiveFieldChange?.invoke(field)
    }

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
            Column {
                Text(
                    text = "Registrar notas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Corte $termId",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GradeInputField(
                    label = "Formativa",
                    value = formativa,
                    isActive = selectedField == "formativa",
                    onValueChange = onFormativaChange,
                    onClick = { selectField("formativa") },
                    modifier = Modifier.weight(1f)
                )

                GradeInputField(
                    label = "Cognitiva",
                    value = cognitiva,
                    isActive = selectedField == "cognitiva",
                    onValueChange = onCognitivaChange,
                    onClick = { selectField("cognitiva") },
                    modifier = Modifier.weight(1f)
                )
            }

            NumericPad(
                onNumberClick = { number ->
                    val currentField = if (selectedField == "formativa") formativa else cognitiva

                    val newValue = when {
                        currentField.isEmpty() -> {
                            if (number in "0".."5") "$number." else ""
                        }

                        currentField.length < 4 -> {
                            currentField + number
                        }

                        else -> currentField
                    }

                    if (newValue.isNotEmpty() || currentField.isEmpty()) {
                        if (selectedField == "formativa") {
                            onFormativaChange(newValue)
                        } else {
                            onCognitivaChange(newValue)
                        }
                    }
                },
                onDeleteClick = {
                    val currentField = if (selectedField == "formativa") formativa else cognitiva

                    if (currentField.isNotEmpty()) {
                        val newValue =
                            if (currentField.endsWith(".")) "" else currentField.dropLast(1)

                        if (selectedField == "formativa") {
                            onFormativaChange(newValue)
                        } else {
                            onCognitivaChange(newValue)
                        }
                    }
                },
                onSaveClick = onSave
            )
        }
    }
}

@Preview(name = "GradeEditor Light", showBackground = true)
@Preview(
    name = "GradeEditor Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun GradeEditorSectionCardPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            GradeEditorSectionCard(
                termId = 3,
                formativa = "3.5",
                cognitiva = "4.0",
                onFormativaChange = {},
                onCognitivaChange = {},
                onSave = {},
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
