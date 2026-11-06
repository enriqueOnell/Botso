package com.onell.botso.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Esquemas de alto contraste con tonos de superficie entonados.
 * El primary (#6A3DF0) es el violeta saturado usado en hero y FAB.
 */
val BotsoLightColors = lightColorScheme(
    primary = Color(0xFF6A3DF0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE7DEFF),
    onPrimaryContainer = Color(0xFF210064),
    secondary = Color(0xFF5B5790),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE4DFEC),
    onSecondaryContainer = Color(0xFF1B1B22),
    surface = Color(0xFFFCF8FF),
    onSurface = Color(0xFF1B1B22),
    onSurfaceVariant = Color(0xFF4B4658),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF6F2FB),
    surfaceContainer = Color(0xFFF0EBF7),
    surfaceContainerHigh = Color(0xFFEAE5F2),
    surfaceContainerHighest = Color(0xFFE4DFEC),
    outline = Color(0xFF7A7587),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD9),
    onErrorContainer = Color(0xFF410002),
)

val BotsoDarkColors = darkColorScheme(
    primary = Color(0xFFCABEFF),
    onPrimary = Color(0xFF2A0A7A),
    primaryContainer = Color(0xFF4A2BB8),
    onPrimaryContainer = Color(0xFFE7DEFF),
    secondary = Color(0xFFC8C3E4),
    onSecondary = Color(0xFF302E4D),
    secondaryContainer = Color(0xFF36343E),
    onSecondaryContainer = Color(0xFFE6E1EC),
    surface = Color(0xFF14121A),
    onSurface = Color(0xFFE6E1EC),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceContainerLowest = Color(0xFF0F0D14),
    surfaceContainerLow = Color(0xFF1C1A23),
    surfaceContainer = Color(0xFF211F28),
    surfaceContainerHigh = Color(0xFF2B2933),
    surfaceContainerHighest = Color(0xFF36343E),
    outline = Color(0xFF938F99),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

/** Colores semánticos que Material no modela: aprobado vs. reprobado. */
@Immutable
data class BotsoSemanticColors(
    val passContainer: Color,
    val onPassContainer: Color,
)

val LightSemanticColors = BotsoSemanticColors(
    passContainer = Color(0xFFD4F7CD),
    onPassContainer = Color(0xFF0B3D12),
)

val DarkSemanticColors = BotsoSemanticColors(
    passContainer = Color(0xFF1E4D1F),
    onPassContainer = Color(0xFFA8F5A0),
)

val LocalBotsoColors = staticCompositionLocalOf { LightSemanticColors }