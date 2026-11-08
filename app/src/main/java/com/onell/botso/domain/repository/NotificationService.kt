package com.onell.botso.domain.repository

/**
 * Contrato para mostrar notificaciones locales.
 */
interface NotificationService {
    fun showNotification(title: String, message: String)
}