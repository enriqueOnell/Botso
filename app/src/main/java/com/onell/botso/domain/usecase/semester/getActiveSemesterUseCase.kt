package com.onell.botso.domain.usecase.semester

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.repository.SemesterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetActiveSemesterUseCase @Inject constructor(
    private val repository: SemesterRepository
) {
    operator fun invoke(): Flow<Semester?> {
        return repository.getActiveSemester()
    }
}