package com.onell.UniApp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.UniApp.data.local.entity.Course
import com.onell.UniApp.data.local.entity.Grade
import com.onell.UniApp.data.local.entity.Semester
import com.onell.UniApp.domain.model.SemesterWithStats
import com.onell.UniApp.domain.repository.UniRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SemestersUiEvent {
    data class OnAddSemester(val name: String, val startDate: Long, val endDate: Long, val isActive: Boolean) : SemestersUiEvent()
    data class OnUpdateSemester(val semester: Semester, val name: String, val startDate: Long, val endDate: Long, val isActive: Boolean) : SemestersUiEvent()
    data class OnDeleteSemester(val semester: Semester) : SemestersUiEvent()
    data class OnAddCourse(val semesterId: Long, val name: String, val dayOfWeek: Int, val startTime: String, val endTime: String, val professor: String, val colorHex: String, val location: String, val isRemote: Boolean) : SemestersUiEvent()
    data class OnEditCourse(val course: Course, val name: String, val dayOfWeek: Int, val startTime: String, val endTime: String, val professor: String, val colorHex: String, val location: String, val isRemote: Boolean) : SemestersUiEvent()
    data class OnDeleteCourseConfirm(val course: Course) : SemestersUiEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SemestersViewModel @Inject constructor(
    private val repository: UniRepository
) : ViewModel() {

    val semestersWithStats: StateFlow<List<SemesterWithStats>> = repository.getAllSemesters()
        .flatMapLatest { semesters ->
            if (semesters.isEmpty()) return@flatMapLatest flowOf(emptyList<SemesterWithStats>())
            
            val statsFlows = semesters.map { semester ->
                repository.getCoursesForSemester(semester.id).flatMapLatest { courses ->
                    if (courses.isEmpty()) {
                        return@flatMapLatest flowOf(SemesterWithStats(semester, emptyList(), 0.0))
                    }
                    
                    val courseGradeFlows = courses.map { course ->
                        repository.getGradesForCourse(course.id).map { grades ->
                            calculateCourseAverage(grades)
                        }
                    }
                    
                    combine(courseGradeFlows) { averages ->
                        val gpa = if (averages.isNotEmpty()) averages.average() else 0.0
                        SemesterWithStats(semester, courses, gpa)
                    }
                }
            }
            combine(statsFlows) { it.toList() }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun onEvent(event: SemestersUiEvent) {
        when (event) {
            is SemestersUiEvent.OnAddSemester -> addSemester(event.name, event.startDate, event.endDate, event.isActive)
            is SemestersUiEvent.OnUpdateSemester -> updateSemester(event.semester, event.name, event.startDate, event.endDate, event.isActive)
            is SemestersUiEvent.OnDeleteSemester -> deleteSemester(event.semester)
            is SemestersUiEvent.OnAddCourse -> addCourse(event.semesterId, event.name, event.dayOfWeek, event.startTime, event.endTime, event.professor, event.colorHex, event.location, event.isRemote)
            is SemestersUiEvent.OnEditCourse -> updateCourse(event.course, event.name, event.dayOfWeek, event.startTime, event.endTime, event.professor, event.colorHex, event.location, event.isRemote)
            is SemestersUiEvent.OnDeleteCourseConfirm -> deleteCourse(event.course)
        }
    }

    private fun addSemester(name: String, startDate: Long, endDate: Long, isActive: Boolean) {
        viewModelScope.launch {
            repository.insertSemester(Semester(name = name, startDate = startDate, endDate = endDate, isActive = isActive))
        }
    }

    private fun updateSemester(semester: Semester, name: String, startDate: Long, endDate: Long, isActive: Boolean) {
        viewModelScope.launch {
            repository.updateSemester(semester.copy(name = name, startDate = startDate, endDate = endDate, isActive = isActive))
        }
    }

    private fun deleteSemester(semester: Semester) {
        viewModelScope.launch {
            repository.deleteSemester(semester)
        }
    }

    private fun addCourse(semesterId: Long, name: String, dayOfWeek: Int, startTime: String, endTime: String, professor: String, colorHex: String, location: String, isRemote: Boolean) {
        viewModelScope.launch {
            repository.insertCourse(
                Course(
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

    private fun updateCourse(course: Course, name: String, dayOfWeek: Int, startTime: String, endTime: String, professor: String, colorHex: String, location: String, isRemote: Boolean) {
        viewModelScope.launch {
            repository.updateCourse(
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
            repository.deleteCourse(course)
        }
    }

    private fun calculateCourseAverage(grades: List<Grade>): Double {
        if (grades.isEmpty()) return 0.0
        
        // Group by term (Corte)
        val gradesByTerm = grades.groupBy { it.termId }
        val termAverages = gradesByTerm.map { (_, termGrades) ->
            termGrades.sumOf { it.score * it.weight }
        }
        
        return if (termAverages.isNotEmpty()) termAverages.average() else 0.0
    }
}
