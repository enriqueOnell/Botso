package com.onell.botso.domain.usecase.session

import com.onell.botso.domain.model.CourseWithSession
import com.onell.botso.domain.repository.ClassSessionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSessionsForDayUseCase @Inject constructor(
    private val repository: ClassSessionRepository
) {
    operator fun invoke(dayOfWeek: Int): Flow<List<CourseWithSession>> {
        return repository.getSessionsForDay(dayOfWeek)
    }
}
