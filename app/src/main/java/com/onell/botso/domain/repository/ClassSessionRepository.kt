package com.onell.botso.domain.repository

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import kotlinx.coroutines.flow.Flow

interface ClassSessionRepository {
    // Class Sessions
    fun getAllClassSessions(): Flow<List<ClassSession>>
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>>
    fun getSessionsForCourse(courseId: String): Flow<List<ClassSessionWithCourse>>
    suspend fun insertClassSession(session: ClassSession)
    suspend fun updateClassSession(session: ClassSession)
    suspend fun deleteClassSession(session: ClassSession)
}