package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.ui.navigation.AppNavigation
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.utils.LocaleUtils

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val app = application as ArogyaApplication

        setContent {
            val languageCode by app.settingsRepository.languageFlow.collectAsState(initial = "en")
            val localizedContext = LocaleUtils.getLocalizedContext(this, languageCode)

            CompositionLocalProvider(LocalContext provides localizedContext) {
                MyApplicationTheme {
                    AppNavigation()
                }
            }
        }
    }
}
