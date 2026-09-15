package com.onell.botso.data.provider

import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import com.onell.botso.domain.model.CourseWithGrades
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.provider.SemesterPdfProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class SemesterPdfProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SemesterPdfProvider {

    override fun generatePdf(semester: Semester, coursesWithGrades: List<CourseWithGrades>): String {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 Size
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint()

        var yPosition = 60f

        // Título
        paint.textSize = 22f
        paint.isFakeBoldText = true
        canvas.drawText("Reporte de Semestre: ${semester.name}", 50f, yPosition, paint)
        yPosition += 40f

        // Fechas
        paint.textSize = 14f
        paint.isFakeBoldText = false
        canvas.drawText("Inicio: ${semester.startDate} | Fin: ${semester.endDate}", 50f, yPosition, paint)
        yPosition += 50f

        // Iterar sobre cursos y notas
        for (courseData in coursesWithGrades) {
            paint.textSize = 18f
            paint.isFakeBoldText = true
            canvas.drawText("Curso: ${courseData.course.name}", 50f, yPosition, paint)
            yPosition += 25f

            paint.textSize = 14f
            paint.isFakeBoldText = false
            canvas.drawText("Promedio: ${String.format("%.2f", courseData.averageGrade)}", 50f, yPosition, paint)
            yPosition += 25f

            if (courseData.grades.isNotEmpty()) {
                canvas.drawText("Notas:", 60f, yPosition, paint)
                yPosition += 20f
                for (grade in courseData.grades) {
                    canvas.drawText("- ${grade.name}: ${grade.score} (Peso: ${grade.weight * 100}%)", 70f, yPosition, paint)
                    yPosition += 20f
                }
            } else {
                canvas.drawText("Sin notas registradas.", 60f, yPosition, paint)
                yPosition += 20f
            }

            yPosition += 20f
        }

        document.finishPage(page)

        val fileName = "Reporte_${semester.name.replace(" ", "_")}.pdf"
        val file = File(context.cacheDir, fileName)
        document.writeTo(FileOutputStream(file))
        document.close()

        return file.absolutePath
    }
}
