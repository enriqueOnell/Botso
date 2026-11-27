package com.onell.botso.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Escala de formas (Shapes) para Botso siguiendo el sistema Expressive Academic Canvas.
 * Determina el radio de curvatura (`RoundedCornerShape`) de los componentes de la interfaz:
 *
 * - `extraSmall` (12.dp): Badges de materia/estado (`PriorityTaskItem`, `TaskStatusBadge`, `GradeItem`) y etiquetas compactas.
 * - `small` (16.dp): Campos del editor de notas (`GradeInputField`), tarjetas internas (`SemesterCourseItem`, `TaskItem`) y píldoras informativas.
 * - `medium` (24.dp): Diálogos (`ConfirmDeleteDialog`), elementos internos de Ajustes (`SettingsThemeModeItem`, `SettingsToggleItem`, `SettingsAboutItem`) y botones del FAB en `BotsoMainApp`.
 * - `large` (32.dp): Tarjetas de sección (`TodayClassSectionCard`, `PendingTasksCard`, `TaskFilterSectionCard`, `SemesterSectionCard`, `CourseSummarySectionCard`) y tarjetas destacadas.
 * - `extraLarge` (40.dp): Tarjetas destacadas y estados vacíos (`PriorityTaskItem`, `SectionEmptyState`).
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
