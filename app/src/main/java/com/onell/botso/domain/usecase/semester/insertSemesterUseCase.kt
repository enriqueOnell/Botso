package com.onell.botso.domain.usecase.semester

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.repository.SemesterRepository
import javax.inject.Inject

class InsertSemesterUseCase @Inject constructor(
    private val repository: SemesterRepository
) {
    suspend operator fun invoke(semester: Semester) {
        return repository.insertSemester(semester)
    }
}