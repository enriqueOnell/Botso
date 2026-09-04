package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingTasksCountUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getPendingTasksCount()
    }
}