package com.onell.botso.ui.uistate

/**
 * Estado de la pantalla de Ajustes.
 *
 * Los valores por defecto coinciden con los de `SettingsRepositoryImpl`
 * (color dinámico y notificaciones activados).
 */
data class SettingsUiState(
    val isDynamicColorEnabled: Boolean = true,
    val isTaskNotificationsEnabled: Boolean = true,
    val isClassNotificationsEnabled: Boolean = true
) {
    constructor(
        isDynamicColorEnabled: Boolean,
        isTaskNotificationsEnabled: Boolean,
        isClassNotificationsEnabled: Boolean,
        p3: Any
    ) : this(isDynamicColorEnabled, isTaskNotificationsEnabled, isClassNotificationsEnabled)
}