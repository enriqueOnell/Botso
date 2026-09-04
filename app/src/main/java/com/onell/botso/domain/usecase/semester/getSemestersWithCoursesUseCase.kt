package com.onell.botso.domain.usecase.semester

import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.domain.repository.SemesterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSemestersWithCoursesUseCase @Inject constructor(
    private val repository: SemesterRepository
) {
    operator fun invoke(): Flow<List<SemesterWithCourse>> {
        return repository.getSemestersWithCourses()
    }
}