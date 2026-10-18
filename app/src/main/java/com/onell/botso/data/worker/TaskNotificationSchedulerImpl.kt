package com.onell.botso.data.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.onell.botso.domain.repository.TaskNotificationScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class TaskNotificationSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TaskNotificationScheduler {
    private val WORK_NAME = "DailyTaskReminderWork"

    override fun scheduleDailyReminder() {


        /*
        val testWorkRequest = androidx.work.OneTimeWorkRequestBuilder<DailyTaskWorker>()
        .setInitialDelay(5, java.util.concurrent.TimeUnit.SECONDS)
        .build()

        WorkManager.getInstance(context).enqueue(testWorkRequest)
        */

        val workRequest = PeriodicWorkRequestBuilder<DailyTaskWorker>(3, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            WORK_NAME,
            ExistingPeriodicWorkPolicy.UPDATE,
            workRequest
        )
    }

    override fun cancelReminder() {
        WorkManager.getInstance(context).cancelUniqueWork(WORK_NAME)
    }
}