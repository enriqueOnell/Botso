package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.CourseEntity
import com.onell.botso.data.local.entity.CourseWithGradesEntity
import kotlinx.coroutines.flow.Flow

/**
 * DAO de cursos.
 *
 * Permite consultar cursos por semestre, por id y todos los cursos, incluyendo la
 * consulta `@Transaction` que devuelve cada curso con sus notas
 * (`CourseWithGradesEntity`). Las escrituras usan `REPLACE` para simplificar el
 * upsert.
 */
@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE semester_id = :semesterId")
    fun getCoursesForSemester(semesterId: String): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: String): Flow<CourseEntity?>

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Transaction
    @Query("SELECT * FROM courses")
    fun getCoursesWithGrades(): Flow<List<CourseWithGradesEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Update
    suspend fun updateCourse(course: CourseEntity)

    @Delete
    suspend fun deleteCourse(course: CourseEntity)
}
