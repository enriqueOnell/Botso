package com.onell.botso.domain.usecase.grade

import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.repository.GradeRepository
import javax.inject.Inject

class DeleteGradeUseCase @Inject constructor(
    private val repository: GradeRepository
) {
    suspend operator fun invoke(grade: Grade) {
        repository.deleteGrade(grade)
    }
}