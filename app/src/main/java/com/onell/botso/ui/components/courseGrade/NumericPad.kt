package com.onell.botso.ui.components.courseGrade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun NumericPad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PadButton(text = "1", modifier = Modifier.weight(1f)) { onNumberClick("1") }
            PadButton(text = "2", modifier = Modifier.weight(1f)) { onNumberClick("2") }
            PadButton(text = "3", modifier = Modifier.weight(1f)) { onNumberClick("3") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PadButton(text = "4", modifier = Modifier.weight(1f)) { onNumberClick("4") }
            PadButton(text = "5", modifier = Modifier.weight(1f)) { onNumberClick("5") }
            PadButton(text = "6", modifier = Modifier.weight(1f)) { onNumberClick("6") }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PadButton(text = "7", modifier = Modifier.weight(1f)) { onNumberClick("7") }
            PadButton(text = "8", modifier = Modifier.weight(1f)) { onNumberClick("8") }
            PadButton(text = "9", modifier = Modifier.weight(1f)) { onNumberClick("9") }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón Guardar (Izquierda del 0)
            Button(
                onClick = onSaveClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = "Guardar"
                )
            }

            PadButton(text = "0", modifier = Modifier.weight(1f)) { onNumberClick("0") }

            Button(
                onClick = onDeleteClick,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp),
                shapes = ButtonDefaults.shapes(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Borrar"
                )
            }
        }
    }
}

@Preview
@Composable
fun NumericPadPreview(){
    NumericPad(
        onNumberClick = {},
        onDeleteClick = {},
        onSaveClick = {}
    )
}