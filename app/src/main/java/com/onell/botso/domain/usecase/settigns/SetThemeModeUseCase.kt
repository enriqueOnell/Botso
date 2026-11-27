package com.onell.botso.domain.usecase.settigns

import com.onell.botso.domain.model.ThemeMode
import com.onell.botso.domain.repository.SettingsRepository
import javax.inject.Inject

/**
 * Caso de uso: persistir el tema elegido por el usuario.
 */
class SetThemeModeUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    suspend operator fun invoke(themeMode: ThemeMode) {
        repository.setThemeMode(themeMode)
    }
}
