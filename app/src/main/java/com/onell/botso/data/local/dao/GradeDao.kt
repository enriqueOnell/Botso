package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.Grade
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Query("SELECT * FROM grades WHERE courseId = :courseId")
    fun getGradesForCourse(courseId: Long): Flow<List<Grade>>

    @Query("SELECT * FROM grades")
    fun getAllGrades(): Flow<List<Grade>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: Grade): Long

    @Update
    suspend fun updateGrade(grade: Grade)

    @Delete
    suspend fun deleteGrade(grade: Grade)
}
