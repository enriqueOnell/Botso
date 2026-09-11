package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.CourseEntity
import com.onell.botso.data.local.entity.CourseWithGradesEntity
import kotlinx.coroutines.flow.Flow

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
