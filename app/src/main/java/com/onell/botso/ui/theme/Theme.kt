package com.onell.botso.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext

/**
 * Tema Compose de Botso.
 *
 * Usa `MaterialExpressiveTheme` con la tipografía, formas y esquema de color
 * propios. Si el usuario activa el color dinámico y el dispositivo es Android
 * 12+, se usan los colores del sistema; en caso contrario se aplica la paleta
 * institucional (`BotsoLightColors` / `BotsoDarkColors`). También publica los
 * colores semánticos de dominio mediante `LocalBotsoColors`.
 */
@Composable
fun BotsoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        darkTheme -> BotsoDarkColors
        else -> BotsoLightColors
    }

    CompositionLocalProvider(
        LocalBotsoColors provides if (darkTheme) DarkSemanticColors else LightSemanticColors,
    ) {
        MaterialExpressiveTheme(
            colorScheme = colorScheme,
            typography = BotsoTypography,
            shapes = BotsoShapes,
            motionScheme = MotionScheme.expressive(),
            content = content,
        )
    }
}