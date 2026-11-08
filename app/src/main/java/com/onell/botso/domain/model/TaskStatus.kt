package com.onell.botso.domain.model

/**
 * Estados posibles de una tarea.
 *
 * `key` es el valor persistido en Room y `label` el texto mostrado en la UI
 * (Por Hacer, En Progreso, Hecho).
 */
enum class TaskStatus(val key: String, val label: String) {
    TODO(key = "TODO", label = "Por Hacer"),
    IN_PROGRESS(key = "IN_PROGRESS", label = "En Progreso"),
    DONE(key = "DONE", label = "Hecho")
}