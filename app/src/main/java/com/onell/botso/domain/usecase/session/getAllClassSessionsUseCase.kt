package com.onell.botso.domain.usecase.session

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.repository.ClassSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllClassSessionsUseCase @Inject constructor(
    private val repository: ClassSessionRepository
) {
    operator fun invoke(): Flow<List<ClassSession>> {
        return repository.getAllClassSessions()
    }
}