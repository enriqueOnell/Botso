package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.repository.CourseRepository
import javax.inject.Inject

/**
 * Caso de uso: eliminar un curso (y en cascada sus notas, tareas y horarios).
 */
class DeleteCourseUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(course: Course) {
        repository.deleteCourse(course)
    }
}