package com.onell.botso.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/** Escala de esquinas extra redondeadas para tarjetas y contenedores. */
val BotsoShapes = Shapes(
    extraSmall = RoundedCornerShape(12.dp),
    small = RoundedCornerShape(16.dp),
    medium = RoundedCornerShape(24.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(40.dp),
)

/** Hero y piezas de máxima expresión. */
val HeroShape = RoundedCornerShape(48.dp)

/** FAB con forma orgánica, no círculo perfecto. */
val FabShape = RoundedCornerShape(30.dp)