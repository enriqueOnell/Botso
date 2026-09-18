package com.onell.botso.domain.usecase.grade

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGradesForCourseUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    operator fun invoke(courseId: String): Flow<CourseWithGrades?> {
        return repository.getGradesForCourse(courseId)
    }
}