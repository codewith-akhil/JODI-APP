package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GreenBlueColorScheme =
  lightColorScheme(
    primary = PrimaryEmerald,
    secondary = PrimaryBlue,
    tertiary = PrimaryTeal,
    background = LightGreenBackground,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = PureWhite,
    onTertiary = PureWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = LightBlue,
    onSurfaceVariant = TextSecondary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false,
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = GreenBlueColorScheme,
    typography = Typography,
    content = content
  )
}
