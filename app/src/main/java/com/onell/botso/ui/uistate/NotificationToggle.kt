package com.onell.botso.ui.uistate

/**
 * Identifica el ajuste de notificaciones que el usuario intenta activar.
 * La UI lo usa para asociar el resultado del permiso del sistema con la
 * preferencia correcta, sin que el ViewModel conozca detalles de Android.
 */
enum class NotificationToggle {
    TASKS,
    CLASSES
}
