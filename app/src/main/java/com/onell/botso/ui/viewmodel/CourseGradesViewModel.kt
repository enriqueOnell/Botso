package com.onell.botso.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.botso.domain.model.Grade
import com.onell.botso.domain.usecase.grade.GetGradesForCourseUseCase
import com.onell.botso.domain.usecase.grade.InsertGradeUseCase
import com.onell.botso.domain.usecase.grade.UpdateGradeUseCase
import com.onell.botso.ui.uistate.CourseGradesUiEvent
import com.onell.botso.ui.uistate.CourseGradesUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseGradesViewModel @Inject constructor(
    private val getGradesForCourseUseCase: GetGradesForCourseUseCase,
    private val insertGradeUseCase: InsertGradeUseCase,
    private val updateGradeUseCase: UpdateGradeUseCase
) : ViewModel() {

    // Un único estado centralizado para dominar la pantalla
    private val _uiState = MutableStateFlow(CourseGradesUiState())
    val uiState: StateFlow<CourseGradesUiState> = _uiState.asStateFlow()

    private var gradesJob: Job? = null

    fun onEvent(event: CourseGradesUiEvent) {
        when (event) {
            is CourseGradesUiEvent.OnSetCourseId -> setCourseId(event.id)
            is CourseGradesUiEvent.OnSetTermId -> setTermId(event.termId)
            is CourseGradesUiEvent.OnUpdateFormativa -> updateStagedFormativa(event.value)
            is CourseGradesUiEvent.OnUpdateCognitiva -> updateStagedCognitiva(event.value)
            is CourseGradesUiEvent.OnSave -> saveGrades()
        }
    }

    private fun setCourseId(id: String) {
        if (_uiState.value.courseId != id) {
            _uiState.update { it.copy(courseId = id) }
            observeGrades(id)
        }
    }

    private fun observeGrades(courseId: String) {
        gradesJob?.cancel() // Cancelamos el flujo anterior si cambiamos de materia
        gradesJob = viewModelScope.launch {
            getGradesForCourseUseCase(courseId).collect { currentGrades ->
                _uiState.update { state ->
                    state.copy(
                        grades = currentGrades,
                        averageScore = calculateTotalAverage(currentGrades)
                    )
                }
                // Refresca las notas editables en pantalla con los nuevos datos
                loadStagedGrades(_uiState.value.currentTermId, currentGrades)
            }
        }
    }

    private fun setTermId(termId: Int) {
        _uiState.update { it.copy(currentTermId = termId) }
        loadStagedGrades(termId, _uiState.value.grades)
    }

    private fun loadStagedGrades(termId: Int, currentGrades: List<Grade>) {
        val termGrades = currentGrades.filter { it.termId == termId }

        val formativa = termGrades.find { it.name.contains("Formativa") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""
        val cognitiva = termGrades.find { it.name.contains("Cognitiva") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""

        _uiState.update {
            it.copy(
                stagedFormativa = formativa,
                stagedCognitiva = cognitiva,
                currentTermAverage = calculateTermAverage(formativa, cognitiva)
            )
        }
    }

    private fun updateStagedFormativa(value: String) {
        if (isValidInput(value)) {
            _uiState.update {
                it.copy(
                    stagedFormativa = value,
                    currentTermAverage = calculateTermAverage(value, it.stagedCognitiva)
                )
            }
        }
    }

    private fun updateStagedCognitiva(value: String) {
        if (isValidInput(value)) {
            _uiState.update {
                it.copy(
                    stagedCognitiva = value,
                    currentTermAverage = calculateTermAverage(it.stagedFormativa, value)
                )
            }
        }
    }

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

    private fun saveGrades() {
        val id = _uiState.value.courseId ?: return
        val state = _uiState.value
        val courseId = state.courseId ?: return
        val termId = state.currentTermId
        val formativaScore = state.stagedFormativa.toDoubleOrNull() ?: 0.0
        val cognitivaScore = state.stagedCognitiva.toDoubleOrNull() ?: 0.0

        val weight = if (termId == 3) 0.20 else 0.15

        viewModelScope.launch {
            saveOrUpdateGrade(id, courseId, termId, "Nota Formativa", formativaScore, weight)
            saveOrUpdateGrade(id, courseId, termId, "Nota Cognitiva", cognitivaScore, weight)
        }
    }

    private suspend fun saveOrUpdateGrade(id: String ,courseId: String, termId: Int, name: String, score: Double, weight: Double) {
        val currentGrades = _uiState.value.grades
        val existingGrade = currentGrades.find { it.termId == termId && it.name == name }

        if (existingGrade != null) {
            updateGradeUseCase(existingGrade.copy(score = score))
        } else {
            // Usamos el modelo de Dominio puro (Grade), adiós a GradeEntity
            insertGradeUseCase(
                Grade(id = id, courseId = courseId, termId = termId, name = name, score = score, weight = weight)
            )
        }
    }

    private fun calculateTotalAverage(grades: List<Grade>): Double {
        if (grades.isEmpty()) return 0.0
        return grades.sumOf { it.score * it.weight }
    }
}