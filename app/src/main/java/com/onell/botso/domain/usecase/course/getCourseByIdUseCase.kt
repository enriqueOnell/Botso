package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Caso de uso: observar un curso por su id.
 */
class GetCourseByIdUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    operator fun invoke(courseId: String): Flow<Course?> {
        return repository.getCourseById(courseId)
    }
}