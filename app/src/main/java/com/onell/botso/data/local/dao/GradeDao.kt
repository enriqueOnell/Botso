package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.GradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Query("SELECT * FROM grades WHERE course_id = :courseId")
    fun getGradesForCourse(courseId: Long): Flow<List<GradeEntity>>

    @Query("SELECT * FROM grades")
    fun getAllGrades(): Flow<List<GradeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity): Long

    @Update
    suspend fun updateGrade(grade: GradeEntity)

    @Delete
    suspend fun deleteGrade(grade: GradeEntity)
}
