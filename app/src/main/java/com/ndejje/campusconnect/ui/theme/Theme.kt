package com.ndejje.campusconnect.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val lightColorScheme = lightColorScheme(
    primary = NdejjeGreen80,
    onPrimary = White,
    primaryContainer = NdejjeGreenContainer,
    onPrimaryContainer = OnSurfaceDark,
    secondary = NdejjeGold80,
    onSecondary = OnSurfaceDark,
    secondaryContainer = NdejjeGoldContainer,
    onSecondaryContainer = OnSurfaceDark,
    background = Cream,
    onBackground = OnSurfaceDark,
    surface = Cream,
    onSurface = OnSurfaceDark,
    error = ErrorRed,
    onError = White
)

private val darkColorScheme = darkColorScheme(
    primary = NdejjeGreenContainer,
    onPrimary = OnSurfaceDark,
    primaryContainer = NdejjeGreen80,
    onPrimaryContainer = White,
    secondary = NdejjeGold80,
    onSecondary = OnSurfaceDark,
    background = OnSurfaceDark,
    onBackground = White,
    surface = OnSurfaceDark,
    onSurface = White,
    error = ErrorRedLight, // Changed to a lighter red for better visibility on dark backgrounds
    onError = OnSurfaceDark
)

@Composable
fun CampusConnectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColorScheme else lightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = campusConnectTypography,
        content = content
    )
}
