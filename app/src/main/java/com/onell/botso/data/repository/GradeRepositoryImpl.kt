package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.GradeDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.repository.GradeRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GradeRepositoryImpl @Inject constructor(
    private val gradeDao: GradeDao
) : GradeRepository {
    override fun getAllGrades(): Flow<List<Grade>> =
        gradeDao.getAllGrades().map { list -> list.map { it.toDomain() } }

    override fun getGradesForCourse(courseId: String): Flow<CourseWithGrades?> =
        gradeDao.getGradesForCourse(courseId).map { entity -> entity.toDomain() }

    override suspend fun insertGrade(grade: Grade) =
        gradeDao.insertGrade(grade.toEntity())

    override suspend fun updateGrade(grade: Grade) =
        gradeDao.updateGrade(grade.toEntity())

    override suspend fun deleteGrade(grade: Grade) =
        gradeDao.deleteGrade(grade.toEntity())
}