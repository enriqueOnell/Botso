package com.onell.botso.data.service

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.onell.botso.R
import com.onell.botso.di.NotificationModule
import com.onell.botso.domain.repository.NotificationService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Implementación de `NotificationService` con `NotificationCompat`.
 *
 * Verifica el permiso `POST_NOTIFICATIONS` antes de publicar y usa el canal
 * creado en `NotificationModule`. Cada notificación recibe un id basado en el
 * tiempo para no sobrescribir las anteriores.
 */
class NotificationServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationManager: NotificationManagerCompat
) : NotificationService {
    override fun showNotification(title: String, message: String) {
        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val notification = NotificationCompat.Builder(context, NotificationModule.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationId = System.currentTimeMillis().toInt()
        notificationManager.notify(notificationId, notification)
    }
}