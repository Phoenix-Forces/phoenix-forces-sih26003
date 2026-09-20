package com.example.myapplication.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = NavyPrimaryLight,
    onPrimary = NavyOnPrimaryLight,
    primaryContainer = NavyPrimaryContainerLight,
    onPrimaryContainer = NavyOnPrimaryContainerLight,
    secondary = LavenderSecondaryLight,
    onSecondary = LavenderOnSecondaryLight,
    secondaryContainer = LavenderSecondaryContainerLight,
    onSecondaryContainer = LavenderOnSecondaryContainerLight,
    background = CharcoalBackgroundLight,
    onBackground = CharcoalOnBackgroundLight,
    surface = CharcoalSurfaceLight,
    onSurface = CharcoalOnSurfaceLight,
    surfaceVariant = CharcoalSurfaceVariantLight,
    onSurfaceVariant = CharcoalOnSurfaceVariantLight,
    error = ErrorLight,
    onError = OnErrorLight,
    errorContainer = ErrorContainerLight,
    onErrorContainer = OnErrorContainerLight,
    outline = OutlineLight
)

private val DarkColorScheme = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = BlueOnPrimaryDark,
    primaryContainer = BluePrimaryContainerDark,
    onPrimaryContainer = BlueOnPrimaryContainerDark,
    secondary = LavenderSecondaryDark,
    onSecondary = LavenderOnSecondaryDark,
    secondaryContainer = LavenderSecondaryContainerDark,
    onSecondaryContainer = LavenderOnSecondaryContainerDark,
    background = CharcoalBackgroundDark,
    onBackground = CharcoalOnBackgroundDark,
    surface = CharcoalSurfaceDark,
    onSurface = CharcoalOnSurfaceDark,
    surfaceVariant = CharcoalSurfaceVariantDark,
    onSurfaceVariant = CharcoalOnSurfaceVariantDark,
    error = ErrorDark,
    onError = OnErrorDark,
    errorContainer = ErrorContainerDark,
    onErrorContainer = OnErrorContainerDark,
    outline = OutlineDark
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // We disable dynamic color to strictly enforce the clinical/premium palette
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}