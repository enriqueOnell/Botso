package com.onell.botso.domain.usecase.task

import com.onell.botso.domain.model.CourseWithTask
import com.onell.botso.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllTasksWithCourseUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<CourseWithTask>> {
        return repository.getAllTasksWithCourse()
    }
}
