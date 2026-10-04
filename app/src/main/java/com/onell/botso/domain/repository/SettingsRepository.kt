package com.onell.botso.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository  {
    val isDynamicColorEnabled: Flow<Boolean>

    suspend fun toggleDynamicColor(enable: Boolean)
}