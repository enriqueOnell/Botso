package com.onell.botso.domain.repository

interface NotificationService {
    fun showNotification(title: String, message: String)
}