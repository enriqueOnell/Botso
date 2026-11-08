package com.onell.botso.domain.usecase.settigns
import com.onell.botso.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Caso de uso: activar/desactivar el color dinámico del tema.
 */
class ToggleDynamicColorThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        repository.toggleDynamicColor(isEnabled)
    }
}