package com.onell.botso.domain.usecase.settigns

import com.onell.botso.domain.repository.ClassAlarmScheduler
import com.onell.botso.domain.repository.SettingsRepository
import com.onell.botso.domain.usecase.session.GetSessionsForDayUseCase
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class ToggleClassNotificationsUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val alarmScheduler: ClassAlarmScheduler,
    private val getSessionsForDayUseCase: GetSessionsForDayUseCase
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        repository.toggleClassNotifications(isEnabled)

        val today = LocalDate.now()
        val clases = getSessionsForDayUseCase(today.dayOfWeek.value).first()

        if (isEnabled) {
            val currentTimeMillis = System.currentTimeMillis()

            clases.forEach { clase ->
                val triggerTimeInMillis = today
                    .atTime(clase.session.startTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                if (triggerTimeInMillis > currentTimeMillis) {
                    alarmScheduler.scheduleClassReminder(
                        classId = clase.course.id,
                        className = clase.course.name,
                        triggerTimeInMillis = triggerTimeInMillis
                    )
                }
            }
        } else {
            clases.forEach { clase ->
                alarmScheduler.cancelClassReminder(clase.course.id)
            }
        }
    }
}