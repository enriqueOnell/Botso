package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Grade // Modelo de Dominio puro
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.model.SemesterWithStats
import com.onell.botso.domain.usecase.course.DeleteCourseUseCase
import com.onell.botso.domain.usecase.course.GetCoursesForSemesterUseCase
import com.onell.botso.domain.usecase.course.InsertCourseUseCase
import com.onell.botso.domain.usecase.course.UpdateCourseUseCase
import com.onell.botso.domain.usecase.grade.GetGradesForCourseUseCase
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
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SemestersViewModel @Inject constructor(
    private val getAllSemestersUseCase: GetAllSemestersUseCase,
    private val getCoursesForSemesterUseCase: GetCoursesForSemesterUseCase,
    private val getGradesForCourseUseCase: GetGradesForCourseUseCase,
    private val insertSemesterUseCase: InsertSemesterUseCase,
    private val updateSemesterUseCase: UpdateSemesterUseCase,
    private val deleteSemesterUseCase: DeleteSemesterUseCase,
    private val insertCourseUseCase: InsertCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase
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

                    combine(courseGradeFlows) { averages ->
                        val gpa = if (averages.isNotEmpty()) averages.average() else 0.0
                        SemesterWithStats(semester, courses, gpa)
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
                event.id,
                event.semesterId,
                event.name,
                event.dayOfWeek,
                event.startTime,
                event.endTime,
                event.professor,
                event.colorHex,
                event.location,
                event.isRemote
            )

            is SemestersUiEvent.OnEditCourse -> updateCourse(
                event.course,
                event.name,
                event.dayOfWeek,
                event.startTime,
                event.endTime,
                event.professor,
                event.colorHex,
                event.location,
                event.isRemote
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
                    id = id,
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
        id: String,
        semesterId: String,
        name: String,
        dayOfWeek: Int,
        startTime: String,
        endTime: String,
        professor: String,
        colorHex: String,
        location: String,
        isRemote: Boolean
    ) {
        viewModelScope.launch {
            insertCourseUseCase(
                Course(
                    id = id,
                    semesterId = semesterId,
                    name = name,
                    code = name.take(3).uppercase(),
                    professor = professor,
                    dayOfWeek = dayOfWeek,
                    startTime = startTime,
                    endTime = endTime,
                    colorHex = colorHex,
                    location = location,
                    isRemote = isRemote
                )
            )
        }
    }

    private fun updateCourse(
        course: Course,
        name: String,
        dayOfWeek: Int,
        startTime: String,
        endTime: String,
        professor: String,
        colorHex: String,
        location: String,
        isRemote: Boolean
    ) {
        viewModelScope.launch {
            updateCourseUseCase(
                course.copy(
                    name = name,
                    code = name.take(3).uppercase(),
                    professor = professor,
                    dayOfWeek = dayOfWeek,
                    startTime = startTime,
                    endTime = endTime,
                    colorHex = colorHex,
                    location = location,
                    isRemote = isRemote
                )
            )
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