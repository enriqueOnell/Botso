package com.onell.botso.domain.repository

interface TaskNotificationScheduler {
    fun scheduleDailyReminder()
    fun cancelReminder()
}