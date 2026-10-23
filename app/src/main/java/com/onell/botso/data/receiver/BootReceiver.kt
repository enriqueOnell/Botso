package com.onell.botso.data.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.onell.botso.domain.repository.SettingsRepository
import com.onell.botso.domain.usecase.settigns.ToggleClassNotificationsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var settingsRepository: SettingsRepository

    @Inject
    lateinit var toggleClassNotificationsUseCase: ToggleClassNotificationsUseCase

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED ||
            intent?.action == "android.intent.action.QUICKBOOT_POWERON") {

            // Los BroadcastReceivers mueren rápido. Usamos una corrutina en IO
            // para leer DataStore y Room de forma segura en segundo plano.
            CoroutineScope(Dispatchers.IO).launch {
                // 1. Verificamos si el usuario tenía el switch encendido
                val isEnabled = settingsRepository.isClassNotificationsEnabled.first()

                // 2. Si estaba encendido, llamamos a tu Caso de Uso para que
                // lea Room y reprograme todo automáticamente.
                if (isEnabled) {
                    toggleClassNotificationsUseCase(true)
                }
            }
        }
    }
}