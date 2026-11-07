package com.onell.botso.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Escala de formas (Shapes) para Botso siguiendo el sistema Expressive Academic Canvas.
 * Determina el radio de curvatura (RoundedCornerShape) de los componentes de la interfaz:
 *
 * - `extraSmall` (12.dp): Chips compactos, etiquetas secundarias y pequeñas insignias.
 * - `small` (16.dp): Campos de formulario (`FilledFormField`, `PickerFormField`), tarjetas de materias (`CourseGradeItem`) y filas interactivas.
 * - `medium` (24.dp): Diálogos de confirmación (`ConfirmDeleteDialog`), selectores de hora (`BotsoTimePickerDialog`) y envolturas de sección.
 * - `large` (32.dp): Tarjetas destacadas del Dashboard (`ClassCard`, `PendingTasksCard`, `PriorityItem`) y contenedores principales de pantallas.
 * - `extraLarge` (40.dp): Paneles flotantes expandidos y banners de contexto a ancho completo.
 */
val BotsoShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp),
)

/** Formas personalizadas de alta expresión fuera de la escala estándar de Material 3: */

/** `HeroShape` (48.dp): Usado en tarjetas de héroe principales y elementos de bienvenida destacados. */
val HeroShape = RoundedCornerShape(48.dp)

/** `FabShape` (30.dp): Usado en Floating Action Buttons (FAB) con geometría orgánica y distintiva. */
val FabShape = RoundedCornerShape(30.dp)
