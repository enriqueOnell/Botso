package com.onell.botso.ui.screens

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.onell.botso.ui.components.settings.AppVersionText
import com.onell.botso.ui.components.settings.SettingsCard
import com.onell.botso.ui.uistate.SettingsUiState
import com.onell.botso.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.triggerTestNotification()
        }
    }

    SettingsContent(
        uiState = uiState,
        onDynamicColor = { isChecked -> viewModel.onDynamicColorToggled(isChecked) },
        onTaskNotifications = { viewModel.onTaskNotificationsToggled(it) },
        onClassNotifications = { viewModel.onClassNotificationsToggled(it) },
        onTestNotification = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            } else {
                viewModel.triggerTestNotification()
            }
        }
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onDynamicColor: (Boolean) -> Unit,
    onTaskNotifications: (Boolean) -> Unit,
    onClassNotifications: (Boolean) -> Unit,
    onTestNotification: () -> Unit
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
                onCheckedChange = onTaskNotifications,
                isActive = uiState.isTaskNotificationsEnabled
            )
        }

        item {
            SettingsCard(
                name = "Notificaciones de Clases",
                onCheckedChange = onClassNotifications,
                isActive = uiState.isClassNotificationsEnabled
            )
        }

        item {
            Button(
                onClick = onTestNotification
            ) {
                Text("Probar Notificación")
            }
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

@Preview(showBackground = true)
@Composable
fun SettingsContentPreview() {
    SettingsContent(
        uiState = SettingsUiState(),
        onDynamicColor = {},
        onTaskNotifications = {},
        onClassNotifications = {},
        onTestNotification = {}
    )
}