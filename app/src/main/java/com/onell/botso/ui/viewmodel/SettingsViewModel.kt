package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.usecase.notification.SendNotificationUseCase
import com.onell.botso.domain.usecase.settigns.GetDynamicColorThemeUseCase
import com.onell.botso.domain.usecase.settigns.ToggleDynamicColorThemeUseCase
import com.onell.botso.ui.uistate.SettingsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getDynamicColorThemeUseCase: GetDynamicColorThemeUseCase,
    private val toggleDynamicColorThemeUseCase: ToggleDynamicColorThemeUseCase,
    private val sendNotificationUseCase: SendNotificationUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init{
        loadSettings()
    }
    fun loadSettings() {
        viewModelScope.launch {
            getDynamicColorThemeUseCase().collect { isEnabled ->
                _uiState.value = _uiState.value.copy(isDynamicColorEnabled = isEnabled)
            }
        }
    }

    fun onDynamicColorToggled(isEnabled: Boolean) {
        viewModelScope.launch {
            toggleDynamicColorThemeUseCase(isEnabled)
        }
    }

    fun triggerTestNotification() {
        sendNotificationUseCase(
            title = "¡Hola desde Botso!",
            message = "Las notificaciones están funcionando correctamente."
        )
    }
}