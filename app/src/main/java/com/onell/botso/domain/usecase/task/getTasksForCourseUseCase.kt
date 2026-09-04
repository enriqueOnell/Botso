package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.Task
import com.onell.botso.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksForCourseUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(courseId: Long): Flow<List<Task>> {
        return repository.getTasksForCourse(courseId)
    }
}