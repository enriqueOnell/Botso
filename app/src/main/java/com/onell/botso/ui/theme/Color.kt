package com.onell.botso.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores institucional para Botso (Expressive Academic Canvas).
 * Extraída y optimizada a partir de la identidad visual original ("uniapp_3.png"):
 * - Púrpura/Violeta (#6A3DF0 / #CFBCFF): Color primario de marca (base inferior del morral).
 * - Menta/Turquesa (#006B5F / #55DBC2): Color secundario (bolsillo frontal y tirantes).
 * - Mostaza/Dorado (#7A5900 / #F5BE48): Color terciario y de resalte (cuerpo superior).
 * - Índigo Oscuro (#131219 / #2E2A3E): Superficies oscuras extraídas de los bordes del icono.
 */
val BotsoLightColors = lightColorScheme(
    primary = Color(0xFF6A3DF0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = Color(0xFF006B5F), // Más oscuro para contraste en modo claro
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF76F8DE), // Menta brillante del bolsillo para contenedores
    onSecondaryContainer = Color(0xFF00201B),
    tertiary = Color(0xFF7A5900), // Dorado oscuro para legibilidad
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFDEA5), // Amarillo mostaza del icono para contenedores
    onTertiaryContainer = Color(0xFF261900),
    surface = Color(0xFFFCFAFF),
    onSurface = Color(0xFF1B1B21),
    onSurfaceVariant = Color(0xFF494551),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF6F3FB),
    surfaceContainer = Color(0xFFF0EDF6),
    surfaceContainerHigh = Color(0xFFEAE7F0),
    surfaceContainerHighest = Color(0xFFE4E1EA),
    outline = Color(0xFF797581),
    outlineVariant = Color(0xFFCAC4D2),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

val BotsoDarkColors = darkColorScheme(
    primary = Color(0xFFCFBCFF),
    onPrimary = Color(0xFF380092),
    primaryContainer = Color(0xFF5216DB),
    onPrimaryContainer = Color(0xFFE9DDFF),
    secondary = Color(0xFF55DBC2), // Menta vibrante del icono destacando en la oscuridad
    onSecondary = Color(0xFF003730),
    secondaryContainer = Color(0xFF005047),
    onSecondaryContainer = Color(0xFF76F8DE),
    tertiary = Color(0xFFF5BE48), // Amarillo mostaza brillante
    onTertiary = Color(0xFF412D00),
    tertiaryContainer = Color(0xFF5D4200),
    onTertiaryContainer = Color(0xFFFFDEA5),
    surface = Color(0xFF131219), // Índigo muy profundo basado en los bordes del icono
    onSurface = Color(0xFFE4E1EA),
    onSurfaceVariant = Color(0xFFCAC4D2),
    surfaceContainerLowest = Color(0xFF0D0C13),
    surfaceContainerLow = Color(0xFF1B1A21),
    surfaceContainer = Color(0xFF1F1D25),
    surfaceContainerHigh = Color(0xFF2A2830),
    surfaceContainerHighest = Color(0xFF35323B),
    outline = Color(0xFF938F9A),
    outlineVariant = Color(0xFF494551),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

/**
 * Colores semánticos específicos de dominio académico.
 */
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