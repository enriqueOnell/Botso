package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.repository.CourseRepository
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    suspend operator fun invoke(course: Course) {
        repository.deleteCourse(course)
    }
}