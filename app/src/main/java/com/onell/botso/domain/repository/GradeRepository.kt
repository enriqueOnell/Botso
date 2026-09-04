package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Grade
import kotlinx.coroutines.flow.Flow

interface GradeRepository {
    // Grades
    fun getAllGrades(): Flow<List<Grade>>
    fun getGradesForCourse(courseId: Long): Flow<List<Grade>>
    suspend fun insertGrade(grade: Grade): Long
    suspend fun updateGrade(grade: Grade)
    suspend fun deleteGrade(grade: Grade)
}