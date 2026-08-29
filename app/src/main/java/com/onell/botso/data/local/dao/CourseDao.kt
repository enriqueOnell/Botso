package com.onell.botso.data.local.dao

import androidx.room.*
import com.onell.botso.data.local.entity.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Query("SELECT * FROM courses WHERE semesterId = :semesterId")
    fun getCoursesForSemester(semesterId: Long): Flow<List<Course>>

    @Query("SELECT * FROM courses WHERE id = :courseId")
    fun getCourseById(courseId: Long): Flow<Course?>

    @Query("SELECT * FROM courses")
    fun getAllCourses(): Flow<List<Course>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: Course): Long

    @Update
    suspend fun updateCourse(course: Course)

    @Delete
    suspend fun deleteCourse(course: Course)
}
