package com.onell.botso.domain.provider

import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Semester

interface SemesterPdfProvider {
    fun generatePdf(semester: Semester, coursesWithGrades: List<CourseWithGrades>): String
}
