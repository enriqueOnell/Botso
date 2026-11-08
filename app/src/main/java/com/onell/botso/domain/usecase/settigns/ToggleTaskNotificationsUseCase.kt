package com.onell.botso.domain.usecase.settigns

import com.onell.botso.domain.repository.SettingsRepository
import com.onell.botso.domain.repository.TaskNotificationScheduler
import javax.inject.Inject

/**
 * Caso de uso: activar/desactivar recordatorios de tareas.
 *
 * Persiste la preferencia y programa o cancela el worker periódico según el
 * nuevo estado.
 */
class ToggleTaskNotificationsUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val scheduler: TaskNotificationScheduler

) {
    suspend operator fun invoke(isEnabled: Boolean) {
        repository.toggleTaskNotifications(isEnabled)
        if (isEnabled) {
            scheduler.scheduleDailyReminder()
        } else {
            scheduler.cancelReminder()
        }
    }
}