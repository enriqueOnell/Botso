package com.onell.botso.domain.provider

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Semester

/**
 * Contrato para generar el PDF de un semestre.
 *
 * Devuelve la ruta absoluta del archivo generado.
 */
interface SemesterPdfProvider {
    fun generatePdf(semester: Semester, coursesWithGrades: List<CourseWithGrades>): String
}
