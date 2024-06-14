package com.qwertyuiop.appentrypoint.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    background = BackgroundLight,
    onBackground = OnBackgroundLight,
    primary = PrimaryLight,
    onPrimary = OnPrimaryLight,
    error = ErrorLight
)

@Composable
fun MainAppTheme(
    content: @Composable () -> Unit
) = MaterialTheme(
    colorScheme = LightColorScheme,
    typography = Typography,
    shapes = Shapes,
    content = content
)
