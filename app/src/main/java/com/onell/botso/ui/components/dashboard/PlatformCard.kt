package com.onell.botso.ui.components.dashboard

import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Launch
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri

/**
 * Tarjeta de acceso a una plataforma académica externa.
 *
 * Abre la URL en Custom Tabs (mejor integración que un navegador embebido).
 */
@Composable
fun PlatformCard(
    modifier: Modifier = Modifier,
    text: String,
    url: String
) {
    val context = LocalContext.current

    Card(
        modifier = modifier
            .clickable {
                val customTabsIntent = CustomTabsIntent.Builder().build()
                customTabsIntent.launchUrl(context, url.toUri())
            }
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(8.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row{
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "Browser",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(Modifier.padding(4.dp))
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row() {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Launch,
                        contentDescription = "Lanzador de la plataforma",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlatformCardPreview() {
    PlatformCard(
        text = "SINU",
        url = ""
    )
}