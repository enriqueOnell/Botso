package com.onell.botso.domain.usecase.settigns


import com.onell.botso.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskNotificationsUseCase @Inject constructor(
    private val repository: SettingsRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isTaskNotificationsEnabled
    }
}