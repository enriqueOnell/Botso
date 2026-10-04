package com.onell.botso.domain.usecase.settigns
import com.onell.botso.domain.repository.SettingsRepository
import javax.inject.Inject

class ToggleDynamicColorThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        repository.toggleDynamicColor(isEnabled)
    }
}