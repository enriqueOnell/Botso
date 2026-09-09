package com.onell.botso.data.repository

import com.onell.botso.data.local.dao.SemesterDao
import com.onell.botso.data.mapper.toDomain
import com.onell.botso.data.mapper.toEntity
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import com.onell.botso.domain.repository.SemesterRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SemesterRepositoryImpl @Inject constructor(
    private val semesterDao: SemesterDao
) : SemesterRepository {
    override fun getAllSemesters(): Flow<List<Semester>> =
        semesterDao.getAllSemesters().map { list -> list.map { entity -> entity.toDomain() } }

    override fun getSemestersWithCourses(): Flow<List<SemesterWithCourse>> {
        return semesterDao.getSemestersWithCourses().map { list ->
            list.map { entity -> entity.toDomain() }
        }
    }
    override fun getActiveSemester(): Flow<Semester?> =
        semesterDao.getActiveSemester().map { it?.toDomain() }

    override suspend fun insertSemester(semester: Semester) =
        semesterDao.insertSemester(semester.toEntity())

    override suspend fun updateSemester(semester: Semester) =
        semesterDao.updateSemester(semester.toEntity())

    override suspend fun deleteSemester(semester: Semester) =
        semesterDao.deleteSemester(semester.toEntity())
}