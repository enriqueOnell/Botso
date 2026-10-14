package com.onell.botso.domain.usecase.notification

import com.onell.botso.domain.repository.NotificationService
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    private val notificationService: NotificationService
) {
    operator fun invoke(title: String, message: String) {
        notificationService.showNotification(title, message)
    }
}