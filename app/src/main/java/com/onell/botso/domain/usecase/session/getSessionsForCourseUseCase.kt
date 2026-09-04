package com.onell.botso.domain.usecase.session

import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.repository.ClassSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionsForCourseUseCase @Inject constructor(
    private val repository: ClassSessionRepository
) {
    operator fun invoke(courseId: Long): Flow<List<ClassSessionWithCourse>> {
        return repository.getSessionsForCourse(courseId)
    }
}