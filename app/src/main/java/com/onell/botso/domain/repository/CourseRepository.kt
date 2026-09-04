package com.onell.botso.domain.repository

import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.ClassSessionWithCourse
import com.onell.botso.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CourseRepository {

    // Courses
    fun getAllCourses(): Flow<List<Course>>
    fun getCoursesForSemester(semesterId: Long): Flow<List<Course>>
    fun getCourseById(courseId: Long): Flow<Course?>
    suspend fun insertCourse(course: Course): Long
    suspend fun updateCourse(course: Course)
    suspend fun deleteCourse(course: Course)
}