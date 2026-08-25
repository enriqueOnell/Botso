package com.onell.UniApp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onell.UniApp.data.local.entity.Grade
import com.onell.UniApp.domain.repository.UniRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class CourseGradesUiEvent {
    data class OnSetCourseId(val id: Long) : CourseGradesUiEvent()
    data class OnSetTermId(val termId: Int) : CourseGradesUiEvent()
    data class OnUpdateFormativa(val value: String) : CourseGradesUiEvent()
    data class OnUpdateCognitiva(val value: String) : CourseGradesUiEvent()
    object OnSave : CourseGradesUiEvent()
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class CourseGradesViewModel @Inject constructor(
    private val repository: UniRepository
) : ViewModel() {

    fun onEvent(event: CourseGradesUiEvent) {
        when (event) {
            is CourseGradesUiEvent.OnSetCourseId -> setCourseId(event.id)
            is CourseGradesUiEvent.OnSetTermId -> setTermId(event.termId)
            is CourseGradesUiEvent.OnUpdateFormativa -> updateStagedFormativa(event.value)
            is CourseGradesUiEvent.OnUpdateCognitiva -> updateStagedCognitiva(event.value)
            is CourseGradesUiEvent.OnSave -> saveGrades()
        }
    }

    private val _courseId = MutableStateFlow<Long?>(null)
    
    val grades: StateFlow<List<Grade>> = _courseId.filterNotNull().flatMapLatest { id ->
        repository.getGradesForCourse(id)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // UI State for staged grades (not yet saved to DB)
    private val _stagedFormativa = MutableStateFlow("")
    val stagedFormativa: StateFlow<String> = _stagedFormativa.asStateFlow()

    private val _stagedCognitiva = MutableStateFlow("")
    val stagedCognitiva: StateFlow<String> = _stagedCognitiva.asStateFlow()

    private val _currentTermId = MutableStateFlow(1)
    val currentTermId: StateFlow<Int> = _currentTermId.asStateFlow()

    val averageScore: StateFlow<Double> = grades.map { currentGrades ->
        calculateTotalAverage(currentGrades)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val currentTermAverage: StateFlow<Double> = combine(
        _stagedFormativa, _stagedCognitiva
    ) { formativa, cognitiva ->
        val f = formativa.toDoubleOrNull() ?: 0.0
        val c = cognitiva.toDoubleOrNull() ?: 0.0
        (f + c) / 2.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun setCourseId(id: Long) {
        if (_courseId.value != id) {
            _courseId.value = id
            loadStagedGrades(id, _currentTermId.value)
        }
    }

    fun setTermId(termId: Int) {
        _currentTermId.value = termId
        _courseId.value?.let { loadStagedGrades(it, termId) }
    }

    private fun loadStagedGrades(courseId: Long, termId: Int) {
        viewModelScope.launch {
            repository.getGradesForCourse(courseId).first().let { currentGrades ->
                val termGrades = currentGrades.filter { it.termId == termId }
                _stagedFormativa.value = termGrades.find { it.name.contains("Formativa") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""
                _stagedCognitiva.value = termGrades.find { it.name.contains("Cognitiva") }?.score?.let { if (it == 0.0) "" else it.toString() } ?: ""
            }
        }
    }

    fun updateStagedFormativa(value: String) {
        if (isValidInput(value)) {
            _stagedFormativa.value = value
        }
    }

    fun updateStagedCognitiva(value: String) {
        if (isValidInput(value)) {
            _stagedCognitiva.value = value
        }
    }

    private fun isValidInput(value: String): Boolean {
        if (value.isEmpty()) return true
        val score = value.toDoubleOrNull() ?: return false
        return score in 0.0..5.0
    }

    fun saveGrades() {
        val courseId = _courseId.value ?: return
        val termId = _currentTermId.value
        val formativaScore = _stagedFormativa.value.toDoubleOrNull() ?: 0.0
        val cognitivaScore = _stagedCognitiva.value.toDoubleOrNull() ?: 0.0

        val weight = if (termId == 3) 0.20 else 0.15

        viewModelScope.launch {
            saveOrUpdateGrade(courseId, termId, "Nota Formativa", formativaScore, weight)
            saveOrUpdateGrade(courseId, termId, "Nota Cognitiva", cognitivaScore, weight)
        }
    }

    private suspend fun saveOrUpdateGrade(courseId: Long, termId: Int, name: String, score: Double, weight: Double) {
        val currentGrades = grades.value
        val existingGrade = currentGrades.find { it.termId == termId && it.name == name }
        if (existingGrade != null) {
            repository.updateGrade(existingGrade.copy(score = score))
        } else {
            repository.insertGrade(Grade(courseId = courseId, termId = termId, name = name, score = score, weight = weight))
        }
    }

    private fun calculateTotalAverage(grades: List<Grade>): Double {
        if (grades.isEmpty()) return 0.0
        return grades.sumOf { it.score * it.weight }
    }
}
