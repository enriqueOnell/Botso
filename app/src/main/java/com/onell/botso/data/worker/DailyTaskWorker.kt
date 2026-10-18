package com.onell.botso.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.onell.botso.domain.repository.NotificationService
import com.onell.botso.domain.repository.SettingsRepository
import com.onell.botso.domain.usecase.task.GetPendingTasksCountUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first
@HiltWorker
class DailyTaskWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParams: WorkerParameters,
    private val notificationService: NotificationService,
    private val settingsRepository: SettingsRepository,
    private val getPendingTasksCountUseCase: GetPendingTasksCountUseCase
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val isEnabled = settingsRepository.isTaskNotificationsEnabled.first()

        if (!isEnabled) {
            return Result.success()
        }

        val pendingTasksCount = getPendingTasksCountUseCase().first()

        if (pendingTasksCount > 0) {
            notificationService.showNotification(
                title = "¡Tienes tareas!",
                message = "Te quedan $pendingTasksCount tareas por completar."
            )
        }
        return Result.success()
    }
}