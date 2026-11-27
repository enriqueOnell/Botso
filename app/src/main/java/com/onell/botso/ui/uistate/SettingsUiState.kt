package com.onell.botso.ui.uistate

import com.onell.botso.domain.model.ThemeMode

/**
 * Estado de la pantalla de Ajustes.
 *
 * Los valores por defecto coinciden con los de `SettingsRepositoryImpl`
 * (tema del sistema, color dinámico y notificaciones activados).
 */
data class SettingsUiState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val isDynamicColorEnabled: Boolean = true,
    val isTaskNotificationsEnabled: Boolean = true,
    val isClassNotificationsEnabled: Boolean = true
) {
    constructor(
        isDynamicColorEnabled: Boolean,
        isTaskNotificationsEnabled: Boolean,
        isClassNotificationsEnabled: Boolean,
        p3: Any
    ) : this(
        themeMode = ThemeMode.SYSTEM,
        isDynamicColorEnabled = isDynamicColorEnabled,
        isTaskNotificationsEnabled = isTaskNotificationsEnabled,
        isClassNotificationsEnabled = isClassNotificationsEnabled
    )
}