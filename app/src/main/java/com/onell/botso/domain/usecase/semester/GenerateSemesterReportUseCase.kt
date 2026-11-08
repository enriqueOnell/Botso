package com.onell.botso.domain.usecase.semester

import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.provider.SemesterPdfProvider
import com.onell.botso.domain.repository.CourseRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * Caso de uso: generar el reporte PDF de un semestre.
 *
 * Obtiene una foto de todos los cursos con notas (`first()`), filtra los del
 * semestre solicitado y delega el dibujo en `SemesterPdfProvider`.
 */
class GenerateSemesterReportUseCase @Inject constructor(
    private val courseRepository: CourseRepository,
    private val pdfProvider: SemesterPdfProvider
) {
    suspend operator fun invoke(semester: Semester): String {
        val allCoursesWithGrades = courseRepository.getCoursesWithGrades().first()
        val semesterCoursesWithGrades = allCoursesWithGrades.filter { it.course.semesterId == semester.id }
        return pdfProvider.generatePdf(semester, semesterCoursesWithGrades)
    }
}
