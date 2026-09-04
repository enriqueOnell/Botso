package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCoursesUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    operator fun invoke(): Flow<List<Course>> {
        return repository.getAllCourses()
    }
}