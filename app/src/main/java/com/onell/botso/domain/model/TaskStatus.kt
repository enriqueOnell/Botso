package com.onell.botso.domain.model

enum class TaskStatus(val key: String, val label: String) {
    TODO(key = "TODO", label = "Por Hacer"),
    IN_PROGRESS(key = "IN_PROGRESS", label = "En Progreso"),
    DONE(key = "DONE", label = "Hecho")
}