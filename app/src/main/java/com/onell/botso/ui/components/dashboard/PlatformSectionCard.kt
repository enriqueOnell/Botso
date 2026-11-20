package com.onell.botso.ui.components.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.ui.theme.BotsoTheme

@Composable
fun PlatformSectionCard(
    modifier: Modifier = Modifier
) {
    val eva = "https://evapresencial.americana.edu.co"
    val sinu = "https://campus.americana.edu.co/cua/sgacampus/#notr62"

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Plataformas",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row {
                PlatformItem(
                    text = "EVA",
                    url = eva,
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.padding(8.dp))
                PlatformItem(
                    text = "SINU",
                    url = sinu,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }


}

@Preview(name = "PlatformSectionCard")
@Composable
private fun PreviewPlatformSectionCard() {
    BotsoTheme {

        PlatformSectionCard()
    }
}