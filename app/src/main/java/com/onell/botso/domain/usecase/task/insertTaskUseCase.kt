package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

/**
 * Caso de uso: registrar una tarea.
 *
 * Regla de negocio: si vence en 3 días o menos, se marca automáticamente como
 * prioritaria aunque el usuario no lo haya indicado.
 */
class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        // Si la tarea vence hoy o en los próximos 3 días, se eleva a prioritaria.
        val daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), task.dueDate)

        val taskToSave = if (daysDifference in 0..3 && !task.isPriority) {
            task.copy(isPriority = true)
        } else {
            task
        }

        return repository.insertTask(taskToSave)

    }
}