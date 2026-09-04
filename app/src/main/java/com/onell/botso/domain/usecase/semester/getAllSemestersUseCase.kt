package com.onell.botso.domain.usecase.semester

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.repository.SemesterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSemestersUseCase @Inject constructor(
    private val repository: SemesterRepository
) {
    operator fun invoke(): Flow<List<Semester>> {
        return repository.getAllSemesters()
    }
}