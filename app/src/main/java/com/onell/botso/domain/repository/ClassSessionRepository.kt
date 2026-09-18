package com.onell.botso.domain.repository

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.CourseWithSession
import kotlinx.coroutines.flow.Flow

interface ClassSessionRepository {
    fun getAllClassSessions(): Flow<List<ClassSession>>
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<CourseWithSession>>
    fun getSessionsForCourse(courseId: String): Flow<List<CourseWithSession>>
    suspend fun insertClassSession(session: ClassSession)
    suspend fun updateClassSession(session: ClassSession)
    suspend fun deleteClassSession(session: ClassSession)
}
