package com.onell.botso.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Sistema tipográfico de Botso (Expressive Academic Canvas).
 * Define los estilos de texto (`Typography`) utilizados en toda la aplicación,
 * priorizando la jerarquía visual, la legibilidad y el peso tipográfico fuerte (`ExtraBold`/`Bold`).
 *
 * Casos de uso de cada estilo tipográfico en la app:
 * - `displayLarge`: Métricas gigantes del Dashboard (`PendingTasksCard`).
 * - `displayMedium` / `displaySmall`: Horario de clase (`ClassItem`) y métricas numéricas principales (`CourseSummarySectionCard`, `CourseGradeItem`).
 * - `headlineLarge`: Título de la barra superior (`CenterAlignedTopAppBar` en `BotsoMainApp`) y fechas destacadas (`PriorityTaskItem`).
 * - `headlineMedium`: Título de la tarjeta héroe (`PendingTasksCard`).
 * - `headlineSmall`: Títulos de tarjetas de sección (`TodayClassSectionCard`, `TaskFilterSectionCard`, `SemesterSectionCard`, `GradesSectionCard`, `SectionEmptyState`).
 * - `titleLarge`: Títulos de ítems principales (materias, tareas, plataformas y filas de Ajustes).
 * - `titleMedium`: Subtítulos y títulos secundarios en barras y formularios.
 * - `bodyLarge`: Texto principal de párrafos, mensajes de estado y campos de formulario.
 * - `bodyMedium`: Descripciones, metadatos, profesor/aula y listas secundarias.
 * - `labelLarge`: Texto en botones de acción y teclado numérico (`NumericPad`).
 */
val BotsoTypography = Typography(
    displayLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 112.sp,
        lineHeight = 96.sp,
        letterSpacing = (-4).sp,
    ),
    displayMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 56.sp,
        lineHeight = 60.sp,
        letterSpacing = (-1.5).sp,
    ),
    displaySmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 48.sp,
        lineHeight = 52.sp,
        letterSpacing = (-1).sp,
    ),
    headlineLarge = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 44.sp,
        lineHeight = 48.sp,
        letterSpacing = (-0.5).sp,
    ),
    headlineMedium = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 40.sp,
    ),
    headlineSmall = TextStyle(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 26.sp,
        lineHeight = 32.sp,
    ),
    titleLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleMedium = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
    labelLarge = TextStyle(
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    ),
)
