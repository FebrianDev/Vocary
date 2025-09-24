package com.febriandev.vocary.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.febriandev.vocary.utils.Constant.DARK_MODE
import com.febriandev.vocary.utils.Constant.PRONOUNCE
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.Prefs.set
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
   // darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    //dynamicColor: Boolean = true,
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

    val systemUiController = rememberSystemUiController()

    val themeMode by ThemeState.themeMode
    val isSystemDark = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemDark
    }

    val colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette

    SideEffect {
        systemUiController.setStatusBarColor(
            color = colorScheme.background,   // atau colorScheme.surface
            darkIcons = !darkTheme
        )
        systemUiController.setNavigationBarColor(
            color = colorScheme.surface,      // biasanya lebih soft
            darkIcons = !darkTheme
        )
    }


    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
        shapes = Shapes
    )
}

object ThemeState {
    var themeMode = mutableStateOf(
        ThemeMode.valueOf(Prefs[DARK_MODE, ThemeMode.SYSTEM.name])
    )
}


enum class ThemeMode(val label: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System")
}


object PronounceState {
    // initial dari Prefs
    var isPronounce = mutableStateOf(Prefs[PRONOUNCE, false])
}