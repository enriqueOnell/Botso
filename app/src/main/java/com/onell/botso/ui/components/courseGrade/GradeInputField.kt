package com.onell.botso.ui.components.courseGrade

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun GradeInputField(
    label: String,
    value: String,
    isActive: Boolean,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(6.dp))

        Box(contentAlignment = Alignment.Center) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                shape = RoundedCornerShape(16.dp),
                readOnly = true,
                singleLine = true,
                placeholder = { Text("0.0") },
                        colors = OutlinedTextFieldDefaults. colors (
                        unfocusedBorderColor =
                            if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                unfocusedTextColor = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                // Un toque de color de fondo translúcido cuando está activo
                unfocusedContainerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer.copy(
                    alpha = 0.2f
                ) else Color.Transparent
            )
            )

            Spacer(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(onClick = onClick)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GradeInputFieldPreview() {
    GradeInputField(
        label = "Corte 1",
        value = "1",
        isActive = true,
        onValueChange = {},
        onClick = {}
    )
}
