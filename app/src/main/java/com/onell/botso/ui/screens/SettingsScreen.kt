package com.onell.botso.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.TaskAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.domain.model.ThemeMode
import com.onell.botso.ui.components.settings.SettingsAboutItem
import com.onell.botso.ui.components.settings.SettingsPermissionNotice
import com.onell.botso.ui.components.settings.SettingsSectionCard
import com.onell.botso.ui.components.settings.SettingsThemeModeItem
import com.onell.botso.ui.components.settings.SettingsToggleItem
import com.onell.botso.ui.theme.BotsoTheme
import com.onell.botso.ui.uistate.NotificationToggle
import com.onell.botso.ui.uistate.SettingsUiState
import com.onell.botso.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

/**
 * Pantalla de Ajustes.
 *
 * Permite activar/desactivar el color dinámico y las notificaciones. La gestión
 * del permiso `POST_NOTIFICATIONS` (solicitud, rationale y caso bloqueado) se
 * resuelve aquí porque depende del sistema; el ViewModel solo persiste el
 * resultado.
 */
private const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier, viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var hasNotificationPermission by remember {
        mutableStateOf(context.hasNotificationPermission())
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasNotificationPermission = context.hasNotificationPermission()
    }

    var pendingToggle by rememberSaveable { mutableStateOf<NotificationToggle?>(null) }
    var showRationaleDialog by rememberSaveable { mutableStateOf(false) }
    var showBlockedDialog by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        val target = pendingToggle
        pendingToggle = null
        hasNotificationPermission = context.hasNotificationPermission()

        target?.let { viewModel.onNotificationPermissionResult(it, isGranted) }

        if (!isGranted) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    "Sin permiso de notificaciones no recibirás recordatorios."
                )
            }
            if (activity != null && !activity.shouldShowNotificationRationale()) {
                showBlockedDialog = true
            }
        }
    }

    val onNotificationToggle: (NotificationToggle, Boolean) -> Unit = { target, isEnabled ->
        when {
            !isEnabled -> viewModel.onNotificationToggled(target, false)
            hasNotificationPermission -> viewModel.onNotificationToggled(target, true)
            else -> {
                pendingToggle = target
                if (activity?.shouldShowNotificationRationale() == true) {
                    showRationaleDialog = true
                } else {
                    permissionLauncher.launch(NOTIFICATION_PERMISSION)
                }
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        SettingsContent(
            uiState = uiState,
            hasNotificationPermission = hasNotificationPermission,
            onThemeModeSelect = viewModel::onThemeModeSelected,
            onDynamicColor = { isChecked -> viewModel.onDynamicColorToggled(isChecked) },
            onNotificationToggle = onNotificationToggle
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(start = 16.dp, end = 16.dp, bottom = 96.dp)
        )
    }

    if (showRationaleDialog) {
        AlertDialog(
            onDismissRequest = {
                showRationaleDialog = false
                pendingToggle = null
            },
            title = { Text("Activa las notificaciones") },
            text = {
                Text(
                    "Botso usa las notificaciones para recordarte tareas pendientes " +
                        "y avisarte cuando empieza una clase."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRationaleDialog = false
                        permissionLauncher.launch(NOTIFICATION_PERMISSION)
                    }
                ) {
                    Text("Permitir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showRationaleDialog = false
                        pendingToggle = null
                    }
                ) {
                    Text("Ahora no")
                }
            }
        )
    }

    if (showBlockedDialog) {
        AlertDialog(
            onDismissRequest = { showBlockedDialog = false },
            title = { Text("Notificaciones bloqueadas") },
            text = {
                Text(
                    "El permiso está desactivado. Puedes habilitarlo desde los " +
                        "ajustes del sistema; mientras tanto Botso no mostrará recordatorios."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showBlockedDialog = false
                        context.openNotificationSettings()
                    }
                ) {
                    Text("Abrir ajustes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showBlockedDialog = false }) {
                    Text("Cerrar")
                }
            }
        )
    }
}

/**
 * Contenido puro de Ajustes.
 *
 * No conoce el ViewModel ni el permiso del sistema: recibe el estado y los
 * callbacks, lo que permite previsualizarlo en claro/oscuro con datos falsos.
 */
@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    hasNotificationPermission: Boolean,
    onThemeModeSelect: (ThemeMode) -> Unit,
    onDynamicColor: (Boolean) -> Unit,
    onNotificationToggle: (NotificationToggle, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 96.dp
        ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(key = "appearance") {
            SettingsSectionCard(
                title = "Apariencia",
                subtitle = "Color y tema de la aplicación",
                icon = Icons.Rounded.Palette,
                iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                iconContentColor = MaterialTheme.colorScheme.primary
            ) {
                SettingsThemeModeItem(
                    selectedMode = uiState.themeMode,
                    onModeSelected = onThemeModeSelect
                )

                SettingsToggleItem(
                    title = "Color dinámico",
                    description = "Adapta Botso a los colores de tu fondo de pantalla cuando estén disponibles.",
                    icon = Icons.Rounded.Palette,
                    checked = uiState.isDynamicColorEnabled,
                    onCheckedChange = onDynamicColor,
                    iconContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    iconContentColor = MaterialTheme.colorScheme.primary
                )
            }
        }

        item(key = "notifications") {
            SettingsSectionCard(
                title = "Notificaciones",
                subtitle = "Elige qué avisos quieres recibir",
                icon = Icons.Rounded.NotificationsActive,
                iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                iconContentColor = MaterialTheme.colorScheme.secondary
            ) {
                SettingsToggleItem(
                    title = "Notificaciones de tareas",
                    description = "Recordatorios de entregas y tareas pendientes.",
                    icon = Icons.Rounded.TaskAlt,
                    checked = uiState.isTaskNotificationsEnabled && hasNotificationPermission,
                    onCheckedChange = { isEnabled ->
                        onNotificationToggle(NotificationToggle.TASKS, isEnabled)
                    },
                    iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    iconContentColor = MaterialTheme.colorScheme.secondary
                )

                SettingsToggleItem(
                    title = "Notificaciones de clases",
                    description = "Avisos antes de que comience cada clase.",
                    icon = Icons.Rounded.Schedule,
                    checked = uiState.isClassNotificationsEnabled && hasNotificationPermission,
                    onCheckedChange = { isEnabled ->
                        onNotificationToggle(NotificationToggle.CLASSES, isEnabled)
                    },
                    iconContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    iconContentColor = MaterialTheme.colorScheme.secondary
                )

                if (!hasNotificationPermission) {
                    SettingsPermissionNotice()
                }
            }
        }

        item(key = "about") {
            SettingsSectionCard(
                title = "Acerca de",
                subtitle = "Información de la aplicación",
                icon = Icons.Rounded.Info,
                iconContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                iconContentColor = MaterialTheme.colorScheme.tertiary
            ) {
                SettingsAboutItem()
            }
        }
    }
}

/**
 * En Android 13+ el permiso de notificaciones es de runtime. En versiones
 * anteriores se concede al instalar la app.
 */
private fun Context.hasNotificationPermission(): Boolean =
    Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
        ContextCompat.checkSelfPermission(
            this,
            NOTIFICATION_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

private fun Activity.shouldShowNotificationRationale(): Boolean =
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ActivityCompat.shouldShowRequestPermissionRationale(
            this,
            NOTIFICATION_PERMISSION
        )

private fun Context.openNotificationSettings() {
    val notificationSettings = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        .putExtra(Settings.EXTRA_APP_PACKAGE, packageName)

    runCatching { startActivity(notificationSettings) }
        .onFailure {
            startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    .setData(Uri.fromParts("package", packageName, null))
            )
        }
}

@Preview(name = "Settings Light", showBackground = true)
@Preview(
    name = "Settings Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsContentPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SettingsContent(
                uiState = SettingsUiState(themeMode = ThemeMode.SYSTEM),
                hasNotificationPermission = true,
                onThemeModeSelect = {},
                onDynamicColor = {},
                onNotificationToggle = { _, _ -> }
            )
        }
    }
}

@Preview(name = "Settings No Permission Light", showBackground = true)
@Preview(
    name = "Settings No Permission Dark",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsContentNoPermissionPreview() {
    BotsoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            SettingsContent(
                uiState = SettingsUiState(
                    themeMode = ThemeMode.DARK,
                    isDynamicColorEnabled = true,
                    isTaskNotificationsEnabled = true,
                    isClassNotificationsEnabled = false
                ),
                hasNotificationPermission = false,
                onThemeModeSelect = {},
                onDynamicColor = {},
                onNotificationToggle = { _, _ -> }
            )
        }
    }
}
