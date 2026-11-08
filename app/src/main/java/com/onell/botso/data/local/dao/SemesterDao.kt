package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.SemesterEntity
import com.onell.botso.data.local.entity.SemesterWithCourseEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO de semestres.
 *
 * Expone consultas reactivas (`Flow`) para observar semestres, semestres con sus
 * cursos/horarios y el semestre activo, además de las operaciones CRUD usadas por
 * `SemesterRepositoryImpl`.
 */
@Dao
interface SemesterDao {
    @Query("SELECT * FROM semesters")
    fun getAllSemesters(): Flow<List<SemesterEntity>>

    @Transaction
    @Query("SELECT * FROM semesters")
    fun getSemestersWithCourses(): Flow<List<SemesterWithCourseEntity>>

    @Query("SELECT * FROM semesters WHERE is_active = 1 LIMIT 1")
    fun getActiveSemester(): Flow<SemesterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSemester(semester: SemesterEntity)

    @Update
    suspend fun updateSemester(semester: SemesterEntity)

    @Delete
    suspend fun deleteSemester(semester: SemesterEntity)
}
