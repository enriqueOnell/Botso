package com.onell.botso.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.onell.botso.domain.model.ThemeMode
import com.onell.botso.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

/**
 * Implementación de `SettingsRepository` sobre Preferences DataStore.
 *
 * Persiste el tema elegido (sistema, claro u oscuro) y tres preferencias
 * booleanas (color dinámico, notificaciones de tareas y de clases). Los errores
 * de E/S se degradan a valores por defecto (`emptyPreferences`) para no romper
 * la UI.
 */
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {

    private object PreferencesKeys {
        val THEME_MODE = stringPreferencesKey("theme_mode")
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color_enabled")
        val TASK_NOTIFICATIONS = booleanPreferencesKey("task_notifications_enabled")
        val CLASS_NOTIFICATIONS = booleanPreferencesKey("class_notifications_enabled")
    }

    override val themeMode: Flow<ThemeMode> = dataStore.data
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { preferences ->
            val storedMode = preferences[PreferencesKeys.THEME_MODE]
            ThemeMode.entries.firstOrNull { it.name == storedMode } ?: ThemeMode.SYSTEM
        }

    override suspend fun setThemeMode(themeMode: ThemeMode) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.THEME_MODE] = themeMode.name
        }
    }

    override val isDynamicColorEnabled: Flow<Boolean> = dataStore.data
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { it[PreferencesKeys.DYNAMIC_COLOR] ?: true }

    override val isTaskNotificationsEnabled: Flow<Boolean> = dataStore.data
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { preferences ->
            preferences[PreferencesKeys.TASK_NOTIFICATIONS] ?: true
        }

    override val isClassNotificationsEnabled: Flow<Boolean> = dataStore.data
        .catch { exception -> if (exception is IOException) emit(emptyPreferences()) else throw exception }
        .map { preferences ->
            preferences[PreferencesKeys.CLASS_NOTIFICATIONS] ?: true
        }

    override suspend fun toggleDynamicColor(isEnabled: Boolean) {
        dataStore.edit { it[PreferencesKeys.DYNAMIC_COLOR] = isEnabled }
    }

    override suspend fun toggleTaskNotifications(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.TASK_NOTIFICATIONS] = isEnabled
        }
    }

    override suspend fun toggleClassNotifications(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.CLASS_NOTIFICATIONS] = isEnabled
        }
    }
}