package com.rahmatsobrian.umkchecker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.rahmatsobrian.umkchecker.data.datastore.UserPreferencesDataStore
import com.rahmatsobrian.umkchecker.presentation.navigation.UmkNavGraph
import com.rahmatsobrian.umkchecker.presentation.theme.UmkCheckerTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Single-activity host for the whole Compose UI. Kept intentionally thin —
 * all business logic lives in ViewModels/UseCases/Repository.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesDataStore: UserPreferencesDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val dynamicColorEnabled by userPreferencesDataStore.isDynamicColorEnabled
                .collectAsState(initial = true)

            UmkCheckerTheme(
                darkTheme = isSystemInDarkTheme(),
                dynamicColor = dynamicColorEnabled
            ) {
                UmkNavGraph()
            }
        }
    }
}
