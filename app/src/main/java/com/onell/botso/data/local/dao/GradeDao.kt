package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.CourseWithGradesEntity
import com.onell.botso.data.local.entity.GradeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GradeDao {
    @Transaction
    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getGradesForCourse(courseId: String): Flow<CourseWithGradesEntity>

    @Query("SELECT * FROM grades")
    fun getAllGrades(): Flow<List<GradeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGrade(grade: GradeEntity)

    @Update
    suspend fun updateGrade(grade: GradeEntity)

    @Delete
    suspend fun deleteGrade(grade: GradeEntity)
}
