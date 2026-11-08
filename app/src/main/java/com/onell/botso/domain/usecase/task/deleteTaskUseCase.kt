package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import javax.inject.Inject

/**
 * Caso de uso: eliminar una tarea.
 */
class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.deleteTask(task)
    }
}