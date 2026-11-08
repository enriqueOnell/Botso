package com.onell.botso

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Clase `Application` de Botso.
 *
 * `@HiltAndroidApp` inicializa el grafo de dependencias de Hilt para toda la app.
 * Además implementa `Configuration.Provider` para que WorkManager use
 * `HiltWorkerFactory` y pueda inyectar dependencias en los workers
 * (por ejemplo, `DailyTaskWorker`).
 */
@HiltAndroidApp
class BotsoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}