package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.ClassSessionDao
import com.onell.botso.data.local.dao.CourseDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.repository.ClassSessionRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ClassSessionRepositoryImpl @Inject constructor(
    private val sessionDao: ClassSessionDao,
) : ClassSessionRepository {

    override fun getAllClassSessions(): Flow<List<ClassSession>> {
        return sessionDao.getAllSessions().map { list -> list.map { it.toDomain() } }
    }

    override fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourse>> {
        return sessionDao.getSessionsForDay(dayOfWeek).map { list -> list.map { it.toDomain() } }
    }

    override fun getSessionsForCourse(courseId: Long): Flow<List<ClassSessionWithCourse>> =
        sessionDao.getSessionsForCourse(courseId).map { list -> list.map { it.toDomain() } }

    override suspend fun insertClassSession(session: ClassSession): Long =
        sessionDao.insertSession(session.toEntity())

    override suspend fun updateClassSession(session: ClassSession) =
        sessionDao.updateSession(session.toEntity())

    override suspend fun deleteClassSession(session: ClassSession) =
        sessionDao.deleteSession(session.toEntity())
}