package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF92400E),
    secondary = TempleGoldDark,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFFBEB),
    onSecondaryContainer = Color(0xFF92400E),
    tertiary = PeacockBlue,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFD1FAE5),
    onTertiaryContainer = Color(0xFF065F46),
    background = PavitraIvory,
    onBackground = TextPrimaryLight,
    surface = WarmCardBg,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = TextSecondaryLight,
    outline = WarmCardBorder,
    outlineVariant = Color(0xFFF3F4F6)
)

private val DarkColorScheme = darkColorScheme(
    primary = SaffronPrimary,
    onPrimary = Color(0xFF1F1206),
    primaryContainer = Color(0xFF78350F),
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = TempleGold,
    onSecondary = Color(0xFF1F1206),
    secondaryContainer = Color(0xFF2C1810),
    onSecondaryContainer = Color(0xFFFFDDB8),
    tertiary = PeacockBlueLight,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF064E3B),
    onTertiaryContainer = Color(0xFFA7F3D0),
    background = DarkTempleBg,
    onBackground = TextPrimaryDark,
    surface = DarkCardBg,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF292524),
    onSurfaceVariant = TextSecondaryDark,
    outline = DarkCardBorder,
    outlineVariant = Color(0xFF292524)
)

@Composable
fun BhaktiSagarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    BhaktiSagarTheme(darkTheme = darkTheme, content = content)
}
