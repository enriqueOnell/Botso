package com.onell.botso.domain.repository

import kotlinx.coroutines.flow.Flow

/**
 * Contrato de las preferencias de usuario (tema y notificaciones).
 */
interface SettingsRepository  {
    val isDynamicColorEnabled: Flow<Boolean>
    suspend fun toggleDynamicColor(isEnabled: Boolean)

    val isTaskNotificationsEnabled: Flow<Boolean>
    suspend fun toggleTaskNotifications(isEnabled: Boolean)

    val isClassNotificationsEnabled: Flow<Boolean>
    suspend fun toggleClassNotifications(isEnabled: Boolean)
}