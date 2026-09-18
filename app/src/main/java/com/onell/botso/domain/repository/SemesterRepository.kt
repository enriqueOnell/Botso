package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import kotlinx.coroutines.flow.Flow

interface SemesterRepository {
    fun getAllSemesters(): Flow<List<Semester>>
    fun getSemestersWithCourses(): Flow<List<SemesterWithCourse>>
    fun getActiveSemester(): Flow<Semester?>
    suspend fun insertSemester(semester: Semester)
    suspend fun updateSemester(semester: Semester)

    suspend fun deleteSemester(semester: Semester)
}