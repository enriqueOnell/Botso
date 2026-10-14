package com.onell.botso.ui.uistate

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