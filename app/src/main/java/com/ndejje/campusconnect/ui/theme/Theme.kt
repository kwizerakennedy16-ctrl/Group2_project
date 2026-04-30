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
    background = SkyBlue,
    onBackground = OnSurfaceDark,
    surface = SkyBlue,
    onSurface = OnSurfaceDark,
    surfaceVariant = SkyBlueVariant,
    onSurfaceVariant = OnSurfaceDark,
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
    background = Black,
    onBackground = White,
    surface = Black,
    onSurface = White,
    surfaceVariant = DarkGreyVariant,
    onSurfaceVariant = White,
    error = ErrorRedLight,
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
