package com.onell.botso.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.onell.botso.domain.repository.NotificationService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ClassAlarmReceiver : BroadcastReceiver() {
    @Inject
    lateinit var notificationService: NotificationService

    override fun onReceive(context: Context?, intent: Intent?) {
        val className = intent?.getStringExtra("EXTRA_CLASS_NAME") ?: "tu clase"

        notificationService.showNotification(
            title = "¡Clase a punto de empezar!",
            message = "En 30 minutos comienza $className. ¡Prepárate!"
        )
    }
}