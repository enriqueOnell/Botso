package com.onell.botso.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.onell.botso.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    private object PreferenceKeys {
        val DYNAMIC_COLOR = booleanPreferencesKey("dynamic_color_enabled")
    }

    override val isDynamicColorEnabled: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] ?: true
        }

    override suspend fun toggleDynamicColor(enable: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.DYNAMIC_COLOR] = enable
        }
    }
}