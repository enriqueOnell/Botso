package com.onell.UniApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.onell.UniApp.domain.repository.UniRepository
import com.onell.UniApp.navigation.UniAppContent
import com.onell.UniApp.ui.theme.UniAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var repository: UniRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LaunchedEffect(Unit) {
                repository.initializeDatabaseIfEmpty()
            }
            UniAppTheme {
                UniAppContent()
            }
        }
    }
}
