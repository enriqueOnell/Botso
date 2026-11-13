package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialShapes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.toShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Tarjeta destacada con el número de tareas pendientes.
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun PendingTasksCard(count: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier
                .align(Alignment.BottomEnd)
                .offset(x = 10.dp, y = (0).dp)
                .size(180.dp)
                .clip(MaterialShapes.Pill.toShape())
                .background(MaterialTheme.colorScheme.primaryContainer)
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column{
                    Text(
                        text = "Tareas \nPendientes",
                        color = MaterialTheme.colorScheme.primaryContainer,
                        style = MaterialTheme.typography.headlineMedium,
                        maxLines = 2
                    )
                }
                Column() {
                    Text(
                        text = count.toString(),
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.displayLarge,
                    )
                }
            }
        }


    }
}

@Preview(showBackground = true)
@Composable
fun PendingTasksCardPreview() {
    BotsoTheme {
        PendingTasksCard(count = 5)
    }
}