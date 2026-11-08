package com.onell.botso.domain.repository

/**
 * Contrato para programar y cancelar alarmas de clase.
 *
 * Recibe el id del curso (para cancelar), el nombre de la materia y el instante
 * de inicio en milisegundos.
 */
interface ClassAlarmScheduler {
    // Le pasaremos el ID (para poder cancelarla después), el nombre de la materia y la hora en milisegundos
    fun scheduleClassReminder(classId: String, className: String, triggerTimeInMillis: Long)
    fun cancelClassReminder(classId: String)
}