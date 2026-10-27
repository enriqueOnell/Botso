package com.onell.botso.domain.usecase.settigns

import com.onell.botso.domain.repository.ClassAlarmScheduler
import com.onell.botso.domain.repository.SettingsRepository
import com.onell.botso.domain.usecase.semester.GetSemestersWithCoursesUseCase
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.TemporalAdjusters
import javax.inject.Inject

class ToggleClassNotificationsUseCase @Inject constructor(
    private val repository: SettingsRepository,
    private val alarmScheduler: ClassAlarmScheduler,
    private val getSemestersWithCoursesUseCase: GetSemestersWithCoursesUseCase
) {
    suspend operator fun invoke(isEnabled: Boolean) {
        repository.toggleClassNotifications(isEnabled)

        val semesters = getSemestersWithCoursesUseCase().first()
        val allCoursesWithSessions = semesters.flatMap { it.courses }

        val currentTimeMillis = System.currentTimeMillis()
        val today = LocalDate.now()

        if (isEnabled) {
            allCoursesWithSessions.forEach { clase ->
                val targetDayOfWeek = java.time.DayOfWeek.of(clase.session.dayOfWeek)
                val targetDate = today.with(TemporalAdjusters.nextOrSame(targetDayOfWeek))

                val triggerTimeInMillis = targetDate
                    .atTime(clase.session.startTime)
                    .atZone(ZoneId.systemDefault())
                    .toInstant()
                    .toEpochMilli()

                val finalTriggerTime = if (triggerTimeInMillis <= currentTimeMillis) {
                    targetDate.plusWeeks(1)
                        .atTime(clase.session.startTime)
                        .atZone(ZoneId.systemDefault())
                        .toInstant()
                        .toEpochMilli()
                } else {
                    triggerTimeInMillis
                }

                alarmScheduler.scheduleClassReminder(
                    classId = clase.course.id,
                    className = clase.course.name,
                    triggerTimeInMillis = finalTriggerTime
                )
            }
        } else {
            allCoursesWithSessions.forEach { clase ->
                alarmScheduler.cancelClassReminder(clase.course.id)
            }
        }
    }
}