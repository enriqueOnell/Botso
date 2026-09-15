package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.ClassSessionEntity
import com.onell.botso.data.local.entity.CourseWithSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassSessionDao {
    @Transaction
    @Query("SELECT DISTINCT c.* FROM courses c INNER JOIN class_sessions s ON c.id = s.course_id WHERE s.course_id = :courseId")
    fun getSessionsForCourse(courseId: String): Flow<List<CourseWithSessionEntity>>

    @Transaction
    @Query("SELECT DISTINCT c.* FROM courses c INNER JOIN class_sessions s ON c.id = s.course_id WHERE s.day_of_week = :dayOfWeek")
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<CourseWithSessionEntity>>

    @Query("SELECT * FROM class_sessions")
    fun getAllSessions(): Flow<List<ClassSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ClassSessionEntity)

    @Update
    suspend fun updateSession(session: ClassSessionEntity)

    @Delete
    suspend fun deleteSession(session: ClassSessionEntity)
}
