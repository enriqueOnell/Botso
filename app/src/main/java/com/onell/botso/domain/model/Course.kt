package com.onell.botso.domain.model

/**
 * Modelo de dominio de un curso/materia.
 *
 * Pertenece a un semestre y guarda el nombre del profesor.
 */
data class Course(
    val id: String,
    val semesterId: String,
    val name: String,
    val professor: String = ""
)