package com.onell.botso.domain.usecase.grade

import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.repository.GradeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllGradesUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    operator fun invoke(): Flow<List<Grade>> {
        return repository.getAllGrades()
    }
}