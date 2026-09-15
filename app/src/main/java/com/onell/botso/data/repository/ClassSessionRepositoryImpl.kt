package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.ClassSessionDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.CourseWithSession
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

    override fun getSessionsForDay(dayOfWeek: Int): Flow<List<CourseWithSession>> {
        return sessionDao.getSessionsForDay(dayOfWeek).map { list ->
            list.flatMap { entity ->
                entity.sessions
                    .filter { it.dayOfWeek == dayOfWeek }
                    .map { sessionEntity ->
                        CourseWithSession(
                            course = entity.course.toDomain(),
                            session = sessionEntity.toDomain()
                        )
                    }
            }
        }
    }

    override fun getSessionsForCourse(courseId: String): Flow<List<CourseWithSession>> =
        sessionDao.getSessionsForCourse(courseId).map { list ->
            list.flatMap { entity ->
                entity.sessions.map { sessionEntity ->
                    CourseWithSession(
                        course = entity.course.toDomain(),
                        session = sessionEntity.toDomain()
                    )
                }
            }
        }

    override suspend fun insertClassSession(session: ClassSession) =
        sessionDao.insertSession(session.toEntity())

    override suspend fun updateClassSession(session: ClassSession) =
        sessionDao.updateSession(session.toEntity())

    override suspend fun deleteClassSession(session: ClassSession) =
        sessionDao.deleteSession(session.toEntity())
}
