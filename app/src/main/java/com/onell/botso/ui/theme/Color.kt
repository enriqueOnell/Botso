package com.onell.botso.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Colores exactos del logo principal de Botso (mochila gris / azul / verde).
 * Son la fuente de la paleta: los esquemas de abajo son escalas tonales Material 3
 * generadas a partir de estos tonos para garantizar contraste (AA o mejor).
 */
object BotsoLogoColors {
    val Outline = Color(0xFF2A1F48)   // contorno índigo oscuro
    val GrayBlue = Color(0xFF8AA4CB)  // cuerpo superior (azul grisáceo)
    val Blue = Color(0xFF4299F5)      // bolsillo frontal y emblema
    val BlueShade = Color(0xFF2C76C9) // sombra del bolsillo
    val Green = Color(0xFF22A064)     // base inferior del morral
}

/**
 * Paleta institucional de Botso (Expressive Academic Canvas), ajustada al logo:
 * - Azul (#0060A9 / #A2C9FF): primario de marca (bolsillo frontal y emblema).
 * - Verde (#006D40 / #69DC9A): secundario (base del morral), también para "aprobado".
 * - Azul grisáceo (#4E5F79 / #B6C8E5): terciario (cuerpo superior del morral).
 * - Superficies neutras con un leve matiz índigo, tomado del contorno del icono.
 */
val BotsoLightColors = lightColorScheme(
    primary = Color(0xFF0060A9),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD3E4FF),
    onPrimaryContainer = Color(0xFF001C38),
    secondary = Color(0xFF006D40),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF86F9B4),
    onSecondaryContainer = Color(0xFF002110),
    tertiary = Color(0xFF4E5F79),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC6D8F7),
    onTertiaryContainer = Color(0xFF081C33),
    surface = Color(0xFFFBF8FE),
    onSurface = Color(0xFF1B1B20),
    onSurfaceVariant = Color(0xFF454650),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF5F3F9),
    surfaceContainer = Color(0xFFEFEDF3),
    surfaceContainerHigh = Color(0xFFE9E7ED),
    surfaceContainerHighest = Color(0xFFE4E1E7),
    outline = Color(0xFF757681),
    outlineVariant = Color(0xFFC5C5D1),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

val BotsoDarkColors = darkColorScheme(
    primary = Color(0xFFA2C9FF),
    onPrimary = Color(0xFF00315C),
    primaryContainer = Color(0xFF004881),
    onPrimaryContainer = Color(0xFFD3E4FF),
    secondary = Color(0xFF69DC9A),
    onSecondary = Color(0xFF00391F),
    secondaryContainer = Color(0xFF00522F),
    onSecondaryContainer = Color(0xFF86F9B4),
    tertiary = Color(0xFFB6C8E5),
    onTertiary = Color(0xFF203149),
    tertiaryContainer = Color(0xFF364860),
    onTertiaryContainer = Color(0xFFD3E3FF),
    surface = Color(0xFF121317),
    onSurface = Color(0xFFE4E1E7),
    onSurfaceVariant = Color(0xFFC5C5D1),
    surfaceContainerLowest = Color(0xFF0D0E12),
    surfaceContainerLow = Color(0xFF1B1B20),
    surfaceContainer = Color(0xFF1F1F24),
    surfaceContainerHigh = Color(0xFF292A2E),
    surfaceContainerHighest = Color(0xFF343439),
    outline = Color(0xFF8F909B),
    outlineVariant = Color(0xFF454650),
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
    passContainer = Color(0xFFC0F5CF),
    onPassContainer = Color(0xFF002D17),
)

val DarkSemanticColors = BotsoSemanticColors(
    passContainer = Color(0xFF12442A),
    onPassContainer = Color(0xFFACE0BC),
)

val LocalBotsoColors = staticCompositionLocalOf { LightSemanticColors }