package com.restaurant.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val Cream = Color(0xFFFFFBF5)
private val Dark = Color(0xFF191815)
private val Orange = Color(0xFFFF7A45)
private val Gold = Color(0xFFFFC96B)

private val LightColors = lightColorScheme(
    primary = Orange,
    secondary = Gold,
    tertiary = Color(0xFF2F8F71),
    background = Cream,
    surface = Color.White,
    onPrimary = Color.White,
)

@Composable
fun OshiTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(colorScheme = LightColors, typography = Typography(), content = content)
}
