package com.onell.botso.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository  {
    val isDynamicColorEnabled: Flow<Boolean>
    suspend fun toggleDynamicColor(isEnabled: Boolean)

    val isTaskNotificationsEnabled: Flow<Boolean>
    suspend fun toggleTaskNotifications(isEnabled: Boolean)

    val isClassNotificationsEnabled: Flow<Boolean>
    suspend fun toggleClassNotifications(isEnabled: Boolean)
}