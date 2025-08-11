package com.febriandev.vocary.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorPalette = lightColorScheme(
    primary = OrangeDeep,
    onPrimary = Color.White,

    secondary = TealDark,
    onSecondary = Color.White,

    background = LightBackground,
    onBackground = LightTextPrimary,

    surface = LightCard,
    onSurface = LightTextSecondary,

    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextTertiary,

    error = AccentRed,
    onError = Color.White,

    tertiary = AccentGreen,
    outline = LightOutline
)

private val DarkColorPalette = darkColorScheme(
    primary = BluePrimaryDark,
    onPrimary = Color.White,

    secondary = TealDark,
    onSecondary = Color.White,

    background = DarkBackground,
    onBackground = DarkTextPrimary,

    surface = DarkCard,
    onSurface = DarkTextSecondary,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextTertiary,

    error = AccentRed,
    onError = Color.Black,

    tertiary = AccentGreen,
    outline = DarkOutline
)


@Composable
fun VocaryTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
//    val colorScheme = when {
//        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
//            val context = LocalContext.current
//            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
//        }
//
//        darkTheme -> DarkColorPalette
//        else -> LightColorPalette
//    }

    val colorScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}