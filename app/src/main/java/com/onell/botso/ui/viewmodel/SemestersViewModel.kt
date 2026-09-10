package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Grade // Modelo de Dominio puro
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats
import com.onell.botso.domain.usecase.course.DeleteCourseUseCase
import com.onell.botso.domain.usecase.course.GetCoursesForSemesterUseCase
import com.onell.botso.domain.usecase.course.InsertCourseUseCase
import com.onell.botso.domain.usecase.course.UpdateCourseUseCase
import com.onell.botso.domain.usecase.grade.GetGradesForCourseUseCase
import com.onell.botso.domain.usecase.session.InsertClassSessionUseCase
import com.onell.botso.domain.usecase.session.UpdateClassSessionUseCase
import com.onell.botso.domain.usecase.session.GetSessionsForCourseUseCase
import com.onell.botso.domain.usecase.semester.DeleteSemesterUseCase
import com.onell.botso.domain.usecase.semester.GetAllSemestersUseCase
import com.onell.botso.domain.usecase.semester.InsertSemesterUseCase
import com.onell.botso.domain.usecase.semester.UpdateSemesterUseCase
import com.onell.botso.ui.uistate.SemestersUiEvent
import com.onell.botso.ui.uistate.SemestersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SemestersViewModel @Inject constructor(
    private val getAllSemestersUseCase: GetAllSemestersUseCase,
    private val getCoursesForSemesterUseCase: GetCoursesForSemesterUseCase,
    private val getGradesForCourseUseCase: GetGradesForCourseUseCase,
    private val getSessionsForCourseUseCase: GetSessionsForCourseUseCase,
    private val insertSemesterUseCase: InsertSemesterUseCase,
    private val updateSemesterUseCase: UpdateSemesterUseCase,
    private val deleteSemesterUseCase: DeleteSemesterUseCase,
    private val insertCourseUseCase: InsertCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val insertClassSessionUseCase: InsertClassSessionUseCase,
    private val updateClassSessionUseCase: UpdateClassSessionUseCase
) : ViewModel() {

    val uiState: StateFlow<SemestersUiState> = getAllSemestersUseCase()
        .flatMapLatest { semesters ->
            if (semesters.isEmpty()) return@flatMapLatest flowOf(SemestersUiState(emptyList()))

            val statsFlows = semesters.map { semester ->
                getCoursesForSemesterUseCase(semester.id).flatMapLatest { courses ->
                    if (courses.isEmpty()) {
                        return@flatMapLatest flowOf(SemesterWithStats(semester, emptyList(), 0.0))
                    }

                    val courseGradeFlows = courses.map { course ->
                        getGradesForCourseUseCase(course.id).map { grades ->
                            calculateCourseAverage(grades)
                        }
                    }
                    
                    val courseSessionFlows = courses.map { course ->
                        getSessionsForCourseUseCase(course.id)
                    }

                    combine(
                        if (courseGradeFlows.isEmpty()) flowOf(emptyList()) else combine(courseGradeFlows) { it.toList() },
                        if (courseSessionFlows.isEmpty()) flowOf(emptyList()) else combine(courseSessionFlows) { it.toList().flatten() }
                    ) { averages, sessionsWithCourses ->
                        val gpa = if (averages.isNotEmpty()) averages.average() else 0.0
                        SemesterWithStats(semester, sessionsWithCourses, gpa)
                    }
                }
            }

            combine(statsFlows) {
                SemestersUiState(semestersWithStats = it.toList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SemestersUiState())

    fun onEvent(event: SemestersUiEvent) {
        when (event) {
            is SemestersUiEvent.OnAddSemester -> addSemester(
                event.id,
                event.name,
                event.startDate,
                event.endDate,
                event.isActive
            )

            is SemestersUiEvent.OnUpdateSemester -> updateSemester(
                event.semester,
                event.name,
                event.startDate,
                event.endDate,
                event.isActive
            )

            is SemestersUiEvent.OnDeleteSemester -> deleteSemester(event.semester)
            is SemestersUiEvent.OnAddCourse -> addCourse(
                event.semesterId,
                event.course,
                event.session
            )

            is SemestersUiEvent.OnEditCourse -> updateCourse(
                event.course,
                event.session
            )

            is SemestersUiEvent.OnDeleteCourseConfirm -> deleteCourse(event.course)
        }
    }

    private fun addSemester(
        id: String,
        name: String,
        startDate: Long,
        endDate: Long,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            insertSemesterUseCase(
                Semester(
                    id = if (id.isBlank()) UUID.randomUUID().toString() else id,
                    name = name,
                    startDate = startDate,
                    endDate = endDate,
                    isActive = isActive
                )
            )
        }
    }

    private fun updateSemester(
        semester: Semester,
        name: String,
        startDate: Long,
        endDate: Long,
        isActive: Boolean
    ) {
        viewModelScope.launch {
            updateSemesterUseCase(
                semester.copy(
                    name = name,
                    startDate = startDate,
                    endDate = endDate,
                    isActive = isActive
                )
            )
        }
    }

    private fun deleteSemester(semester: Semester) {
        viewModelScope.launch {
            deleteSemesterUseCase(semester)
        }
    }

    private fun addCourse(
        semesterId: String,
        course: Course,
        session: ClassSession
    ) {
        viewModelScope.launch {
            val courseId = if (course.id.isBlank()) UUID.randomUUID().toString() else course.id
            val courseToInsert = course.copy(id = courseId, semesterId = semesterId)
            insertCourseUseCase(courseToInsert)
            insertClassSessionUseCase(session.copy(id = if (session.id.isBlank()) UUID.randomUUID().toString() else session.id, courseId = courseId))
        }
    }

    private fun updateCourse(
        course: Course,
        session: ClassSession
    ) {
        viewModelScope.launch {
            updateCourseUseCase(course)
            updateClassSessionUseCase(session.copy(courseId = course.id))
        }
    }

    private fun deleteCourse(course: Course) {
        viewModelScope.launch {
            deleteCourseUseCase(course)
        }
    }

    // El cálculo ahora purga la dependencia de Room y exige clases de Dominio
    private fun calculateCourseAverage(grades: List<Grade>): Double {
        if (grades.isEmpty()) return 0.0

        val gradesByTerm = grades.groupBy { it.termId }
        val termAverages = gradesByTerm.map { (_, termGrades) ->
            termGrades.sumOf { it.score * it.weight }
        }

        return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
    }
}