package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import javax.inject.Inject

class InsertTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        return repository.insertTask(task)
    }
}