package com.onell.botso.ui.components.grades

import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Grade

/**
 * Datos falsos compartidos por los previews de la pantalla de Notas.
 */
internal fun previewCourseWithGrades(
    courseName: String = "Bases de Datos",
    grades: List<Grade> = previewPassingGrades()
): CourseWithGrades = CourseWithGrades(
    course = Course(
        id = "course-preview",
        semesterId = "semester-preview",
        name = courseName,
        professor = "Ing. Laura Restrepo"
    ),
    grades = grades
)

internal fun previewPassingGrades(): List<Grade> = listOf(
    previewGrade("t1-formativa", "Nota Formativa", 4.5, 1),
    previewGrade("t1-cognitiva", "Nota Cognitiva", 4.0, 1),
    previewGrade("t2-formativa", "Nota Formativa", 3.8, 2),
    previewGrade("t2-cognitiva", "Nota Cognitiva", 4.2, 2),
    previewGrade("t3-formativa", "Nota Formativa", 4.0, 3),
    previewGrade("t3-cognitiva", "Nota Cognitiva", 4.5, 3)
)

internal fun previewFailingGrades(): List<Grade> = listOf(
    previewGrade("t1-formativa", "Nota Formativa", 2.0, 1),
    previewGrade("t1-cognitiva", "Nota Cognitiva", 2.5, 1),
    previewGrade("t2-formativa", "Nota Formativa", 1.8, 2),
    previewGrade("t2-cognitiva", "Nota Cognitiva", 2.2, 2),
    previewGrade("t3-formativa", "Nota Formativa", 2.4, 3),
    previewGrade("t3-cognitiva", "Nota Cognitiva", 2.0, 3)
)

internal fun previewGrade(
    id: String,
    name: String,
    score: Double,
    termId: Int,
    weight: Double = if (termId == 3) 0.20 else 0.15
): Grade = Grade(
    id = id,
    courseId = "course-preview",
    name = name,
    score = score,
    weight = weight,
    termId = termId
)
