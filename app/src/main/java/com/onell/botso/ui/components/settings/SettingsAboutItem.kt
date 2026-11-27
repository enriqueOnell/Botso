package com.onell.botso.ui.components.settings

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.onell.botso.R
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Tarjeta de identidad de Botso dentro de Ajustes.
 *
 * Integra el logo oficial, el nombre, la descripción y la versión instalada.
 * La versión llega como slot para que el componente siga siendo presentacional.
 */
@Composable
fun SettingsAboutItem(
    modifier: Modifier = Modifier,
    versionContent: @Composable () -> Unit = {
        AppVersionText(
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    },
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(72.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = R.drawable.botso_logo),
                        contentDescription = "Logo de Botso",
                        modifier = Modifier.size(56.dp),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Botso",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Tu compañero para organizar el semestre.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    shape = MaterialTheme.shapes.extraSmall,
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    contentColor = MaterialTheme.colorScheme.onTertiaryContainer
                ) {
                    Box(modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)) {
                        versionContent()
                    }
                }
            }
        }
    }
}

@Preview(name = "SettingsAboutItem Light", showBackground = true)
@Preview(
    name = "SettingsAboutItem Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsAboutItemPreview() {
    BotsoTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            SettingsAboutItem()
        }
    }
}
