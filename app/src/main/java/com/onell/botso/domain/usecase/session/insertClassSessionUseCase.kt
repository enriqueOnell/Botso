package com.onell.botso.domain.usecase.session

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.repository.ClassSessionRepository
import javax.inject.Inject

/**
 * Caso de uso: registrar un horario de clase.
 */
class InsertClassSessionUseCase @Inject constructor(
    private val repository: ClassSessionRepository
) {
    suspend operator fun invoke(session: ClassSession) {
        return repository.insertClassSession(session)
    }
}