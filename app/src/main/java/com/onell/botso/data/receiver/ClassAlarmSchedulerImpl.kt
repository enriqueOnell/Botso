package com.onell.botso.data.receiver

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.onell.botso.domain.repository.ClassAlarmScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

/**
 * Programador de alarmas de clase con `AlarmManager`.
 *
 * Calcula la hora de aviso (30 minutos antes del inicio), usa alarmas exactas
 * cuando el sistema lo permite y cae a alarmas inexactas en Android 12+ si no
 * hay permiso. El `PendingIntent` se identifica con el hash del id del curso para
 * poder cancelarlo después.
 */
class ClassAlarmSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ClassAlarmScheduler {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun scheduleClassReminder(classId: String, className: String, triggerTimeInMillis: Long) {
        val thirtyMinutesInMillis = 30 * 60 * 1000L
        val alarmTime = triggerTimeInMillis - thirtyMinutesInMillis

        if (alarmTime <= System.currentTimeMillis()) {
            return
        }

        val intent = Intent(context, ClassAlarmReceiver::class.java).apply {
            putExtra("EXTRA_CLASS_NAME", className)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            classId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )
            } else {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    alarmTime,
                    pendingIntent
                )
            }
        } else {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                pendingIntent
            )
        }
    }

    override fun cancelClassReminder(classId: String) {
        val intent = Intent(context, ClassAlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            classId.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}
