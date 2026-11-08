package com.onell.botso.domain.usecase.settigns

import com.onell.botso.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso: observar la preferencia de color dinámico.
 */
class GetDynamicColorThemeUseCase @Inject constructor(
    private val repository: SettingsRepository
){
    operator fun invoke(): Flow<Boolean> = repository.isDynamicColorEnabled
}