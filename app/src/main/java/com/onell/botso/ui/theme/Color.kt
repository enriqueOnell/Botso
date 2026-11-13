package com.onell.botso.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores institucional para Botso (Expressive Academic Canvas).
 * Inspirada en la ilustración multitonal del morral académico:
 * - Púrpura/Violeta (#6A3DF0 / #4F378A): Color primario de marca (base inferior del morral).
 * - Turquesa/Menta (#008073 / #14B8A6): Color secundario (bolsillo frontal y tirantes).
 * - Amarillo/Dorado (#B45309 / #FACC15): Color terciario y de resalte (cuerpo superior).
 * - Índigo Oscuro (#14121A / #1C1B22): Superficies y bordes oscuros profundos.
 *
 * Casos de uso de los tokens principales en la app:
 * - `primary` / `onPrimary`: Botones de acción principal, Floating Action Buttons (FAB), bordes activos y estados seleccionados.
 * - `primaryContainer` / `onPrimaryContainer`: Tarjetas destacadas, pestañas activas (Tabs), chips de selección.
 * - `secondary` / `onSecondary`: Elementos secundarios, tarjetas de plataforma, chips de estado "En Curso".
 * - `secondaryContainer` / `onSecondaryContainer`: Contenedores secundarios y badges informativos.
 * - `tertiary` / `onTertiary`: Resaltado de tareas prioritarias, insignias de métricas y acentos.
 * - `surface` / `onSurface`: Fondo general de pantallas y tipografía principal de lectura.
 * - `surfaceContainerLowest`: Superficie blanca/oscura pura para tarjetas de contenido elevado (`CourseGradeItem`, `SettingsCard`, inputs activos).
 * - `surfaceContainerLow`: Fondo tonal bajo para campos de texto (`FilledFormField`) y estados vacíos suaves.
 * - `surfaceContainerHigh`: Superficie para filas de tareas pendientes, elementos secundarios y botones de cancelar.
 * - `surfaceContainerHighest`: Círculos contenedores de iconos y elementos desactivados.
 * - `error` / `onError` / `errorContainer` / `onErrorContainer`: Alertas de eliminación, tareas vencidas y calificaciones reprobatorias (< 3.0).
 */
val BotsoLightColors = lightColorScheme(
    primary = Color(0xFF6A3DF0),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFE9DDFF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = Color(0xFF008073),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCFBF1),
    onSecondaryContainer = Color(0xFF00201B),
    tertiary = Color(0xFFB45309),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF451A03),
    surface = Color(0xFFFCF8FF),
    onSurface = Color(0xFF1C1B22),
    onSurfaceVariant = Color(0xFF494551),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF7F2FB),
    surfaceContainer = Color(0xFFF1ECF7),
    surfaceContainerHigh = Color(0xFFEBE5F2),
    surfaceContainerHighest = Color(0xFFE5DFEC),
    outline = Color(0xFF7A7582),
    outlineVariant = Color(0xFFCBC4D2),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF410002),
)

val BotsoDarkColors = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378A),
    onPrimaryContainer = Color(0xFFE9DDFF),
    secondary = Color(0xFF5EEAD4),
    onSecondary = Color(0xFF003731),
    secondaryContainer = Color(0xFF005047),
    onSecondaryContainer = Color(0xFFCCFBF1),
    tertiary = Color(0xFFFDE047),
    onTertiary = Color(0xFF451A03),
    tertiaryContainer = Color(0xFF78350F),
    onTertiaryContainer = Color(0xFFFEF3C7),
    surface = Color(0xFF14121A),
    onSurface = Color(0xFFE6E1EC),
    onSurfaceVariant = Color(0xFFCAC4D0),
    surfaceContainerLowest = Color(0xFF0F0D14),
    surfaceContainerLow = Color(0xFF1C1A23),
    surfaceContainer = Color(0xFF211F28),
    surfaceContainerHigh = Color(0xFF2B2933),
    surfaceContainerHighest = Color(0xFF36343E),
    outline = Color(0xFF938F99),
    outlineVariant = Color(0xFF49454E),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
)

/**
 * Colores semánticos específicos de dominio académico que Material Design no modela por defecto:
 * - `passContainer` / `onPassContainer`: Usado para destacar calificaciones aprobatorias (promedio >= 3.0)
 *   y estados académicos positivos ("En Curso").
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
