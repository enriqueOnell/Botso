package com.onell.botso.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * Sistema tipográfico de Botso (Expressive Academic Canvas).
 * Define los estilos de texto (Typography) utilizados en toda la aplicación,
 * priorizando la jerarquía visual, la legibilidad y el peso tipográfico fuerte (ExtraBold/Bold)
 * característico de los diseños académicos modernos.
 *
 * Casos de uso de cada estilo tipográfico en la app:
 * - `displayLarge`: Indicadores numéricos gigantes de calificaciones y promedios totales (`CircularGradeProgress`).
 * - `displayMedium` / `displaySmall`: Encabezados numéricos y métricas principales de impacto visual.
 * - `headlineLarge`: Títulos principales en tarjetas destacadas de clase (`ClassCard`).
 * - `headlineMedium`: Títulos de la barra superior de navegación (`CenterAlignedTopAppBar`).
 * - `headlineSmall`: Cabeceras de hojas inferiores (`SheetHeader`), títulos de diálogos y tarjetas de resumen (`PendingTasksCard`).
 * - `titleLarge`: Títulos de secciones importantes y nombres principales.
 * - `titleMedium`: Nombres de materias (`CourseGradeItem`) y subtítulos destacados.
 * - `bodyLarge`: Texto principal de párrafos y descripciones extensas.
 * - `bodyMedium`: Texto introducido en campos de formulario (`FilledFormField`), notas y listas.
 * - `labelLarge`: Texto en botones de acción y botoneras inferiores (`SheetFormButtons`, `DialogActionButton`).
 * - `labelSmall`: Insignias de porcentaje (`GradeInputField`), chips de estado y metadatos secundarios.
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
