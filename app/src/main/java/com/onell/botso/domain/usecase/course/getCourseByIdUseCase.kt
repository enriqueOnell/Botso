package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCourseByIdUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    operator fun invoke(courseId: Long): Flow<Course?> {
        return repository.getCourseById(courseId)
    }
}