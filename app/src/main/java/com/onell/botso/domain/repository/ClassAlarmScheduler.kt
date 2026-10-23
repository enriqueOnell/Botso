package com.onell.botso.domain.repository

interface ClassAlarmScheduler {
    // Le pasaremos el ID (para poder cancelarla después), el nombre de la materia y la hora en milisegundos
    fun scheduleClassReminder(classId: String, className: String, triggerTimeInMillis: Long)
    fun cancelClassReminder(classId: String)
}