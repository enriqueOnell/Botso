package com.onell.botso.domain.usecase.session

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.repository.ClassSessionRepository
import javax.inject.Inject

class UpdateClassSessionUseCase @Inject constructor(
    private val repository: ClassSessionRepository
) {
    suspend operator fun invoke(session: ClassSession) {
        repository.updateClassSession(session)
    }
}