package com.example.tusecogoals.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = PrimaryColor,
    onPrimary = WhiteColor,
    secondary = SeaFoamGreen,
    onSecondary = WhiteColor,
    background = WhiteColor,
    onBackground = SecondaryPrimary,
    surface = WhiteColor,
    onSurface = SecondaryPrimary
)

private val DarkColors = darkColorScheme(
    primary = PrimaryColor,
    onPrimary = SecondaryPrimary,
    secondary = MossGreen,
    onSecondary = WhiteColor,
    background = SecondaryPrimary,
    onBackground = WhiteColor,
    surface = SecondaryPrimary,
    onSurface = WhiteColor
)

@Composable
fun TusEcoGoalsTheme(
    darkTheme: Boolean = false, // Toggle based on system settings or user preference
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    androidx.compose.material3.MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Add typography
        content = content
    )
}
