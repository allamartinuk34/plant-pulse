package com.plntpls.appssxdc.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = SurfaceTint,
    onPrimaryContainer = TextPrimary,
    secondary = Secondary,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceTint,
    onSecondaryContainer = TextPrimary,
    tertiary = Accent,
    onTertiary = TextPrimary,
    background = BgBase,
    onBackground = TextPrimary,
    surface = SurfacePaper,
    onSurface = TextPrimary,
    surfaceVariant = BgSunk,
    onSurfaceVariant = TextSecondary,
    outline = BorderHair,
    outlineVariant = BorderHair,
    error = Alert,
    onError = OnPrimary
)

private val DarkScheme = darkColorScheme(
    primary = Accent,
    onPrimary = SplashTop,
    primaryContainer = PrimaryDeep,
    onPrimaryContainer = OnPrimary,
    secondary = Secondary,
    onSecondary = SplashTop,
    secondaryContainer = DarkSurface,
    onSecondaryContainer = OnPrimary,
    tertiary = Accent,
    onTertiary = SplashTop,
    background = DarkBase,
    onBackground = OnPrimary,
    surface = DarkSurface,
    onSurface = OnPrimary,
    surfaceVariant = DarkSurface,
    onSurfaceVariant = Accent,
    outline = PrimaryDeep,
    outlineVariant = PrimaryDeep,
    error = Alert,
    onError = SplashTop
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        typography = AppTypography,
        content = content
    )
}
