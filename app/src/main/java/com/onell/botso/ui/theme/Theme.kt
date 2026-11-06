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