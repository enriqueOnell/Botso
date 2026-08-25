package com.onell.UniApp.data.local.dao

import androidx.room.*
import com.onell.UniApp.data.local.entity.ClassSession
import kotlinx.coroutines.flow.Flow

@Dao
interface ClassSessionDao {
    @Query("SELECT * FROM class_sessions WHERE courseId = :courseId")
    fun getSessionsForCourse(courseId: Long): Flow<List<ClassSession>>

    @Query("SELECT * FROM class_sessions WHERE dayOfWeek = :dayOfWeek")
    fun getSessionsForDay(dayOfWeek: Int): Flow<List<ClassSession>>

    @Query("SELECT * FROM class_sessions")
    fun getAllSessions(): Flow<List<ClassSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: ClassSession): Long

    @Update
    suspend fun updateSession(session: ClassSession)

    @Delete
    suspend fun deleteSession(session: ClassSession)
}
