package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.ClassSessionEntity
import com.onell.botso.data.local.entity.ClassSessionWithCourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassSessionDao {
    @Transaction
    @Query("SELECT * FROM class_sessions WHERE course_id = :courseId")
    fun getSessionsForCourse(courseId: String): Flow<List<ClassSessionWithCourseEntity>>

    @Transaction
    @Query("SELECT * FROM class_sessions WHERE day_of_week = :dayOfWeek")
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSessionWithCourseEntity>>

    @Query("SELECT * FROM class_sessions")
    fun getAllSessions(): Flow<List<ClassSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ClassSessionEntity)

    @Update
    suspend fun updateSession(session: ClassSessionEntity)

    @Delete
    suspend fun deleteSession(session: ClassSessionEntity)
}
