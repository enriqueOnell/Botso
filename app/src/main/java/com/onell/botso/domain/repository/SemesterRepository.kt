package com.onell.botso.domain.repository

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithCourse
import kotlinx.coroutines.flow.Flow

/**
 * Contrato de acceso a semestres.
 *
 * La capa de dominio solo conoce esta interfaz; la implementación real vive en
 * `data/repository` y se enlaza con Hilt.
 */
interface SemesterRepository {
    fun getAllSemesters(): Flow<List<Semester>>
    fun getSemestersWithCourses(): Flow<List<SemesterWithCourse>>
    fun getActiveSemester(): Flow<Semester?>
    suspend fun insertSemester(semester: Semester)
    suspend fun updateSemester(semester: Semester)

    suspend fun deleteSemester(semester: Semester)
}