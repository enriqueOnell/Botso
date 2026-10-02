package com.onell.botso.ui.components.courseGrade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradeInputSection(
    termId: Int,
    formativa: String,
    cognitiva: String,
    onFormativaChange: (String) -> Unit,
    onCognitivaChange: (String) -> Unit,
    onSave: () -> Unit
) {
    val percentage = if (termId == 3) "20%" else "15%"

    var activeField by remember { mutableStateOf("formativa") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    GradeInputField(
                        label = "Formativa ($percentage)",
                        value = formativa,
                        isActive = activeField == "formativa",
                        onValueChange = onFormativaChange,
                        onClick = { activeField = "formativa" },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    GradeInputField(
                        label = "Cognitiva ($percentage)",
                        value = cognitiva,
                        isActive = activeField == "cognitiva",
                        onValueChange = onCognitivaChange,
                        onClick = { activeField = "cognitiva" },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            NumericPad(
                onNumberClick = { number ->
                    val currentField = if (activeField == "formativa") formativa else cognitiva

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
                        if (activeField == "formativa") {
                            onFormativaChange(newValue)
                        } else {
                            onCognitivaChange(newValue)
                        }
                    }
                },
                onDeleteClick = {
                    val currentField = if (activeField == "formativa") formativa else cognitiva

                    if (currentField.isNotEmpty()) {
                        val newValue =
                            if (currentField.endsWith(".")) "" else currentField.dropLast(1)

                        if (activeField == "formativa") {
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

@Preview(showBackground = true)
@Composable
fun GradeInputPreview() {
    GradeInputSection(
        termId = 3,
        formativa = "3.5",
        cognitiva = "4.0",
        onFormativaChange = {},
        onCognitivaChange = {},
        onSave = {}
    )
}
