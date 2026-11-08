package com.onell.botso.domain.repository

/**
 * Contrato para programar/cancelar el recordatorio periódico de tareas.
 */
interface TaskNotificationScheduler {
    fun scheduleDailyReminder()
    fun cancelReminder()
}