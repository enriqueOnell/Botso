package com.onell.botso.domain.repository

import com.onell.botso.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de las preferencias de usuario (tema y notificaciones).
 */
interface SettingsRepository  {
    val themeMode: Flow<ThemeMode>
    suspend fun setThemeMode(themeMode: ThemeMode)

    val isDynamicColorEnabled: Flow<Boolean>
    suspend fun toggleDynamicColor(isEnabled: Boolean)

    val isTaskNotificationsEnabled: Flow<Boolean>
    suspend fun toggleTaskNotifications(isEnabled: Boolean)

    val isClassNotificationsEnabled: Flow<Boolean>
    suspend fun toggleClassNotifications(isEnabled: Boolean)
}