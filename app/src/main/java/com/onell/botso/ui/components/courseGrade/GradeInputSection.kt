package com.onell.botso.ui.components.courseGrade

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            GradeInputField(
                label = "Nota Formativa ($percentage)",
                value = formativa,
                onValueChange = onFormativaChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            GradeInputField(
                label = "Nota Cognitiva ($percentage)",
                value = cognitiva,
                onValueChange = onCognitivaChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Guardar Notas")
            }
        }
    }
}