package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.ClassSession
import com.onell.botso.domain.model.Course
import com.onell.botso.domain.model.Semester
import com.onell.botso.domain.usecase.course.DeleteCourseUseCase
import com.onell.botso.domain.usecase.course.InsertCourseUseCase
import com.onell.botso.domain.usecase.course.UpdateCourseUseCase
import com.onell.botso.domain.usecase.semester.DeleteSemesterUseCase
import com.onell.botso.domain.usecase.semester.GetAllSemestersUseCase
import com.onell.botso.domain.usecase.semester.GetSemestersWithCoursesUseCase
import com.onell.botso.domain.usecase.semester.InsertSemesterUseCase
import com.onell.botso.domain.usecase.semester.UpdateSemesterUseCase
import com.onell.botso.domain.usecase.session.InsertClassSessionUseCase
import com.onell.botso.domain.usecase.session.UpdateClassSessionUseCase
import com.onell.botso.ui.uistate.SemestersUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class SemestersViewModel @Inject constructor(
    private val getAllSemestersUseCase: GetAllSemestersUseCase,
    private val getSemestersWithCoursesUseCase: GetSemestersWithCoursesUseCase,
    private val insertSemesterUseCase: InsertSemesterUseCase,
    private val updateSemesterUseCase: UpdateSemesterUseCase,
    private val deleteSemesterUseCase: DeleteSemesterUseCase,
    private val insertCourseUseCase: InsertCourseUseCase,
    private val updateCourseUseCase: UpdateCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val insertClassSessionUseCase: InsertClassSessionUseCase,
    private val updateClassSessionUseCase: UpdateClassSessionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<SemestersUiState>(SemestersUiState.Loading)
    val uiState: StateFlow<SemestersUiState> = _uiState.asStateFlow()

    init {
        loadSemestersAndCourses()
    }

    private fun loadSemestersAndCourses() {
        viewModelScope.launch {
            try {
                combine(
                    getAllSemestersUseCase(),
                    getSemestersWithCoursesUseCase(),
                ) {
                    allSemester, courseForSemester ->
                        SemestersUiState.Success(
                            allSemester = allSemester,
                            semesterWithCourses = courseForSemester,
                        )
                }
                    .collect { updatedState -> _uiState.value = updatedState }


            } catch (e: Exception) {
                _uiState.value =
                    SemestersUiState.Error("Error al cargar los semestres: ${e.message}")
            }
        }
    }

    fun addSemester(semester: Semester) {
        viewModelScope.launch {
            try {
                insertSemesterUseCase(semester)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error adding semester: ${e.message}")
            }
        }
    }

    fun updateSemester(semester: Semester) {
        viewModelScope.launch {
            try {
                updateSemesterUseCase(semester)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error updating semester: ${e.message}")
            }
        }
    }

    fun deleteSemester(semester: Semester) {
        viewModelScope.launch {
            try {
                deleteSemesterUseCase(semester)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error deleting semester: ${e.message}")
            }
        }
    }

    fun addCourse(course: Course, session: ClassSession) {
        viewModelScope.launch {
            try {
                insertCourseUseCase(course)
                insertClassSessionUseCase(session)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error adding course: ${e.message}")
            }
        }
    }

    fun updateCourse(course: Course, session: ClassSession) {
        viewModelScope.launch {
            try {
                updateCourseUseCase(course)
                updateClassSessionUseCase(session)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error updating course: ${e.message}")
            }
        }
    }

    fun deleteCourse(course: Course) {
        viewModelScope.launch {
            try {
                deleteCourseUseCase(course)
            } catch (e: Exception) {
                _uiState.value = SemestersUiState.Error("Error deleting course: ${e.message}")
            }
        }
    }


}