package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.usecase.grade.GetGradesForCourseUseCase
import com.onell.botso.domain.usecase.grade.InsertGradeUseCase
import com.onell.botso.domain.usecase.grade.UpdateGradeUseCase
import com.onell.botso.ui.uistate.CourseGradesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class CourseGradesViewModel @Inject constructor(
    private val getGradesForCourseUseCase: GetGradesForCourseUseCase,
    private val insertGradeUseCase: InsertGradeUseCase,
    private val updateGradeUseCase: UpdateGradeUseCase
) : ViewModel() {
    // Tu estructura preferida para el estado
    private val _uiState = MutableStateFlow<CourseGradesUiState>(CourseGradesUiState.Loading)
    val uiState: StateFlow<CourseGradesUiState> = _uiState.asStateFlow()

    // =========================================================================
    // INICIALIZACIÓN
    // =========================================================================

    fun loadCourseData(courseId: String) {
        viewModelScope.launch {
            try {
                getGradesForCourseUseCase(courseId).collect { courseData ->
                    if (courseData != null) {
                        _uiState.update { currentState ->
                            // Rescatamos el corte actual si ya existía un Success, sino usamos el Corte 1
                            val termId = (currentState as? CourseGradesUiState.Success)?.currentTermId ?: 1

                            val termGrades = courseData.grades.filter { it.termId == termId }
                            val formativa = termGrades.find { it.name.contains("Formativa") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""
                            val cognitiva = termGrades.find { it.name.contains("Cognitiva") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""

                            // Emitimos el éxito absoluto
                            CourseGradesUiState.Success(
                                courseData = courseData,
                                currentTermId = termId,
                                stagedFormativa = formativa,
                                stagedCognitiva = cognitiva,
                                currentTermAverage = calculateTermAverage(formativa, cognitiva)
                            )
                        }
                    } else {
                        _uiState.value = CourseGradesUiState.Error("No se encontró la información de la materia.")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = CourseGradesUiState.Error("Error de base de datos: ${e.message}")
            }
        }
    }

    // =========================================================================
    // ACCIONES DIRECTAS DE LA UI
    // =========================================================================

    fun setTermId(termId: Int) {
        _uiState.update { currentState ->
            if (currentState is CourseGradesUiState.Success) {
                val currentGrades = currentState.courseData?.grades ?: emptyList()
                val termGrades = currentGrades.filter { it.termId == termId }

                val formativa = termGrades.find { it.name.contains("Formativa") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""
                val cognitiva = termGrades.find { it.name.contains("Cognitiva") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""

                currentState.copy(
                    currentTermId = termId,
                    stagedFormativa = formativa,
                    stagedCognitiva = cognitiva,
                    currentTermAverage = calculateTermAverage(formativa, cognitiva)
                )
            } else currentState
        }
    }

    fun updateStagedFormativa(value: String) {
        if (isValidInput(value)) {
            _uiState.update { currentState ->
                if (currentState is CourseGradesUiState.Success) {
                    currentState.copy(
                        stagedFormativa = value,
                        currentTermAverage = calculateTermAverage(value, currentState.stagedCognitiva)
                    )
                } else currentState
            }
        }
    }

    fun updateStagedCognitiva(value: String) {
        if (isValidInput(value)) {
            _uiState.update { currentState ->
                if (currentState is CourseGradesUiState.Success) {
                    currentState.copy(
                        stagedCognitiva = value,
                        currentTermAverage = calculateTermAverage(currentState.stagedFormativa, value)
                    )
                } else currentState
            }
        }
    }

    fun saveGrades() {
        val state = _uiState.value as? CourseGradesUiState.Success ?: return
        val courseId = state.courseData?.course?.id ?: return
        val termId = state.currentTermId

        val formativaScore = state.stagedFormativa.toDoubleOrNull() ?: 0.0
        val cognitivaScore = state.stagedCognitiva.toDoubleOrNull() ?: 0.0

        val weight = if (termId == 3) 0.20 else 0.15

        viewModelScope.launch {
            val currentGrades = state.courseData.grades
            saveOrUpdateGrade(courseId, termId, "Nota Formativa", formativaScore, weight, currentGrades)
            saveOrUpdateGrade(courseId, termId, "Nota Cognitiva", cognitivaScore, weight, currentGrades)
        }
    }

    private suspend fun saveOrUpdateGrade(courseId: String, termId: Int, name: String, score: Double, weight: Double, currentGrades: List<Grade>) {
        val existingGrade = currentGrades.find { it.termId == termId && it.name == name }

        if (existingGrade != null) {
            updateGradeUseCase(existingGrade.copy(score = score))
        } else {
            insertGradeUseCase(
                Grade(id = UUID.randomUUID().toString(), courseId = courseId, termId = termId, name = name, score = score, weight = weight)
            )
        }
    }

    // =========================================================================
    // UTILIDADES DE UI
    // =========================================================================

    private fun calculateTermAverage(formativa: String, cognitiva: String): Double {
        val f = formativa.toDoubleOrNull() ?: 0.0
        val c = cognitiva.toDoubleOrNull() ?: 0.0
        return (f + c) / 2.0
    }

    private fun isValidInput(value: String): Boolean {
        if (value.isEmpty()) return true
        val score = value.toDoubleOrNull() ?: return false
        return score in 0.0..5.0
    }
}