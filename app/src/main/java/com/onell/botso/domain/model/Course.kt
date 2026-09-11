package com.onell.botso.domain.model

data class Course(
    val id: String,
    val semesterId: String,
    val name: String,
    val professor: String = ""
)