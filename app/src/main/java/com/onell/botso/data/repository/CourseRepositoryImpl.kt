package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.CourseDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.repository.CourseRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CourseRepositoryImpl @Inject constructor(
    private val courseDao: CourseDao
) : CourseRepository {
    override fun getAllCourses(): Flow<List<Course>> {
        return courseDao.getAllCourses().map { list -> list.map { entity -> entity.toDomain() } }
    }

    override fun getCoursesForSemester(semesterId: String): Flow<List<Course>> {
        return courseDao.getCoursesForSemester(semesterId)
            .map { list -> list.map { entity -> entity.toDomain() } }
    }

    override fun getCourseById(courseId: String): Flow<Course?> {
        return courseDao.getCourseById(courseId).map { entity -> entity?.toDomain() }
    }

    override fun getCoursesWithGrades(): Flow<List<CourseWithGrades>> {
        return courseDao.getCoursesWithGrades().map { list -> list.map { entity -> entity.toDomain() } }
    }

    override suspend fun insertCourse(course: Course) {
        return courseDao.insertCourse(course.toEntity())
    }

    override suspend fun updateCourse(course: Course) {
        courseDao.updateCourse(course.toEntity())
    }

    override suspend fun deleteCourse(course: Course) {
        courseDao.deleteCourse(course.toEntity())
    }
}
