package com.onell.botso.domain.usecase.course

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.repository.CourseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoursesWithGradesUseCase @Inject constructor(
    private val repository: CourseRepository
) {
    operator fun invoke(): Flow<List<CourseWithGrades>> {
        return repository.getCoursesWithGrades()
    }
}
