package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme =
  darkColorScheme(
    primary = RosePrimary,
    secondary = GoldAccent,
    tertiary = CrimsonRed,
    background = DarkCardSurface,
    surface = DarkCardSurface,
    onPrimary = PureWhite,
    onSecondary = DarkCardSurface,
    onBackground = PureWhite,
    onSurface = PureWhite,
    surfaceVariant = DarkCardSurface,
    onSurfaceVariant = LightGold,
  )

private val LightColorScheme =
  lightColorScheme(
    primary = DeepBurgundy,
    secondary = GoldAccent,
    tertiary = RosePrimary,
    background = WarmBackground,
    surface = PureWhite,
    onPrimary = PureWhite,
    onSecondary = TextPrimary,
    onTertiary = PureWhite,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    surfaceVariant = BorderLight,
    onSurfaceVariant = TextSecondary,
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = false, // Use consistent luxurious warm ivory theme
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit,
) {
  val colorScheme = LightColorScheme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

