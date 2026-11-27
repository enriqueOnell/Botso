package com.onell.botso.domain.model

/**
 * Preferencia de tema elegida por el usuario.
 *
 * - [SYSTEM] sigue la configuración de claro/oscuro del dispositivo.
 * - [LIGHT] fuerza el esquema claro de Botso.
 * - [DARK] fuerza el esquema oscuro de Botso.
 *
 * El modelo es puro: las etiquetas y los iconos viven en la capa de UI.
 */
enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}
