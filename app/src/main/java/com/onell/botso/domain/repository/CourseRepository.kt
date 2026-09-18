package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    fun getAllCourses(): Flow<List<Course>>
    fun getCoursesForSemester(semesterId: String): Flow<List<Course>>
    fun getCourseById(courseId: String): Flow<Course?>
    fun getCoursesWithGrades(): Flow<List<CourseWithGrades>>
    suspend fun insertCourse(course: Course)
    suspend fun updateCourse(course: Course)
    suspend fun deleteCourse(course: Course)
}
