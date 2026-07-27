package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val LifeIsTheaterColorScheme = darkColorScheme(
    primary = VelvetCrimson,
    onPrimary = IvoryText,
    primaryContainer = DarkBurgundy,
    onPrimaryContainer = StageGold,
    secondary = StageGold,
    onSecondary = TheaterBackground,
    secondaryContainer = BurnishedGold,
    onSecondaryContainer = IvoryText,
    tertiary = GoldenAmber,
    background = TheaterBackground,
    onBackground = IvoryText,
    surface = StageSurface,
    onSurface = IvoryText,
    surfaceVariant = VelvetCharcoal,
    onSurfaceVariant = MutedIvory,
    outline = StageGold
)

@Composable
fun LifeIsTheaterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LifeIsTheaterColorScheme,
        typography = Typography,
        content = content
    )
}
