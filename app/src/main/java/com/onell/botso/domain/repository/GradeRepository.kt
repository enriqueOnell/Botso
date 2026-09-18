package com.onell.botso.domain.repository

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    fun getAllGrades(): Flow<List<Grade>>
    fun getGradesForCourse(courseId: String): Flow<CourseWithGrades?>
    suspend fun insertGrade(grade: Grade)
    suspend fun updateGrade(grade: Grade)
    suspend fun deleteGrade(grade: Grade)
}