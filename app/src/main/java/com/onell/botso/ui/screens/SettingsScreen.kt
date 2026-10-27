package com.onell.botso.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.settings.AppVersionText
import com.onell.botso.ui.components.settings.SettingsCard
import com.onell.botso.ui.uistate.NotificationToggle
import com.onell.botso.ui.uistate.SettingsUiState
import com.onell.botso.ui.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch

private const val NOTIFICATION_PERMISSION = Manifest.permission.POST_NOTIFICATIONS

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = LocalActivity.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // El permiso vive en el sistema: se consulta siempre al volver a la pantalla,
    // nunca se cachea en el ViewModel.
    var hasNotificationPermission by remember {
        mutableStateOf(context.hasNotificationPermission())
    }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        hasNotificationPermission = context.hasNotificationPermission()
    }

    // Ajuste pendiente de resolver el permiso. La UI recuerda a quién
    // corresponde el resultado del diálogo del sistema.
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
            // Si el sistema ya no muestra el diálogo, el permiso quedó
            // bloqueado definitivamente y solo se puede recuperar en Ajustes.
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

    Box(modifier = Modifier.fillMaxSize()) {
        SettingsContent(
            uiState = uiState,
            hasNotificationPermission = hasNotificationPermission,
            onDynamicColor = { isChecked -> viewModel.onDynamicColorToggled(isChecked) },
            onNotificationToggle = onNotificationToggle
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
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

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    hasNotificationPermission: Boolean,
    onDynamicColor: (Boolean) -> Unit,
    onNotificationToggle: (NotificationToggle, Boolean) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(4.dp),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text(
                text = "Apariencia",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        item {
            SettingsCard(
                name = "Color Dinámico",
                onCheckedChange = onDynamicColor,
                isActive = uiState.isDynamicColorEnabled
            )
        }

        item {
            Text(
                text = "Notificaciones",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            SettingsCard(
                name = "Notificaciones de Tareas",
                onCheckedChange = { isEnabled ->
                    onNotificationToggle(NotificationToggle.TASKS, isEnabled)
                },
                isActive = uiState.isTaskNotificationsEnabled && hasNotificationPermission
            )
        }

        item {
            SettingsCard(
                name = "Notificaciones de Clases",
                onCheckedChange = { isEnabled ->
                    onNotificationToggle(NotificationToggle.CLASSES, isEnabled)
                },
                isActive = uiState.isClassNotificationsEnabled && hasNotificationPermission
            )
        }

        item {
            Text(
                text = "Acerca de",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Botso",
                        fontWeight = FontWeight.SemiBold
                    )
                    AppVersionText()
                }
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

@Preview(showBackground = true)
@Composable
fun SettingsContentPreview() {
    SettingsContent(
        uiState = SettingsUiState(),
        hasNotificationPermission = false,
        onDynamicColor = {},
        onNotificationToggle = { _, _ -> },
    )
}
