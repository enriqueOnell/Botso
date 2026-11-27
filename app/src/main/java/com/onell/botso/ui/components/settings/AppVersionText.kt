package com.onell.botso.ui.components.settings

import android.content.res.Configuration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.onell.botso.BuildConfig
import com.onell.botso.ui.theme.BotsoTheme

/**
 * Texto con la versión instalada de la app (`BuildConfig.VERSION_NAME` y
 * `VERSION_CODE`).
 *
 * Es puramente presentacional: acepta estilo y color para integrarse en un
 * badge o en una fila informativa.
 */
@Composable
fun AppVersionText(
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val versionInfo = remember {
        "Versión ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})"
    }

    Text(
        text = versionInfo,
        modifier = modifier,
        style = style,
        color = color
    )
}

@Preview(name = "AppVersionText Light", showBackground = true)
@Preview(
    name = "AppVersionText Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun AppVersionTextPreview() {
    BotsoTheme {
        AppVersionText()
    }
}
