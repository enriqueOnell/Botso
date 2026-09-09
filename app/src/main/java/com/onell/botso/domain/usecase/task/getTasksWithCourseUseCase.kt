package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.TaskWithCourse
import com.onell.botso.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksWithCourseUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(courseId: String): Flow<List<TaskWithCourse>> {
        return repository.getTasksWithCourse(courseId)
    }
}