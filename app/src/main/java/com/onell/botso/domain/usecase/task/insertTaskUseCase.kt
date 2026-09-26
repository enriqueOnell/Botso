package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        val daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), task.dueDate)

        val taskToSave = if (daysDifference in 0..3 && !task.isPriority) {
            task.copy(isPriority = true)
        } else {
            task
        }

        return repository.insertTask(taskToSave)

    }
}