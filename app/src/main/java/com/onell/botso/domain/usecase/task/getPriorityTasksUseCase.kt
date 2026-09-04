package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPriorityTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(courseId: Long): Flow<List<TaskWithCourse>> {
        return repository.getPriorityTasks(courseId)
    }
}