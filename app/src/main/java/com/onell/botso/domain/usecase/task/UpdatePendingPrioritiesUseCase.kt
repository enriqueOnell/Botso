package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import jakarta.inject.Inject
import java.time.LocalDate
import java.time.temporal.ChronoUnit

class UpdatePendingTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: List<Task>) {
        task.filter { task ->
            val daysDifference = ChronoUnit.DAYS.between(LocalDate.now(), task.dueDate)

            !task.isPriority && daysDifference in 0..3
        }.forEach { taskToIUpdate ->

            val updateTask = taskToIUpdate.copy(isPriority = true)

            repository.updateTask(updateTask)
        }

    }
}
