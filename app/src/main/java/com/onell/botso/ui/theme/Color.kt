package com.onell.botso.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Paleta de colores institucional para Botso (Expressive Academic Canvas).
 * Define esquemas de color claros y oscuros (Light y Dark) ajustados para accesibilidad (WCAG)
 * y reducción de fatiga visual durante largas sesiones de estudio.
 *
 * Casos de uso de los tokens principales en la app:
 * - `primary` / `onPrimary`: Botones de acción principal, Floating Action Buttons (FAB), bordes activos y estados seleccionados.
 * - `primaryContainer` / `onPrimaryContainer`: Tarjetas destacadas, pestañas activas (Tabs), chips de selección.
 * - `secondary` / `onSecondary`: Elementos secundarios, iconos de apoyo y metadatos informativos.
 * - `secondaryContainer` / `onSecondaryContainer`: Contenedores secundarios y estados en curso.
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
    surfaceContainerHighest = Color(0xFF36343B),
    outline = Color(0xFF938F99),
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
