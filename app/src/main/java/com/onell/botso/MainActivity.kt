package com.onell.botso

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.navigation.BotsoMainApp
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.viewmodel.SettingsViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Punto de entrada de la aplicación y única Activity del proyecto.
 *
 * Responsabilidades:
 * - Habilitar dibujo edge-to-edge.
 * - Observar `SettingsViewModel` para aplicar el tema (color dinámico on/off).
 * - Delegar toda la navegación y el contenido a `BotsoMainApp`.
 *
 * La anotación `@AndroidEntryPoint` permite que Hilt inyecte dependencias en la
 * Activity y en los ViewModels obtenidos con `by viewModels()`.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val settingsState by settingsViewModel.uiState.collectAsStateWithLifecycle()
            BotsoTheme(
                dynamicColor = settingsState.isDynamicColorEnabled
            ) {
                BotsoMainApp()
            }
        }
    }
}
