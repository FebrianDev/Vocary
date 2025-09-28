package com.febriandev.vocary.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.febriandev.vocary.ui.theme.ThemeMode
import com.febriandev.vocary.ui.theme.ThemeState
import kotlinx.coroutines.delay

@Composable
fun LoadingDialogContent() {

    val themeMode by ThemeState.themeMode
    val isSystemDark = isSystemInDarkTheme()

    val darkTheme = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemDark
    }

    val composition by rememberLottieComposition(LottieCompositionSpec.Asset(if (darkTheme) "loading_dark.json" else "loading_vocabulary.json"))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    val loadingTexts = listOf(
        "Preparing your vocabulary...",
        "Connecting to AI...",
        "Generating words\nbased on your topic...",
        "Getting definitions from dictionary...",
        "Almost done!"
    )

    var currentTextIndex by remember { mutableIntStateOf(0) }

    // Ganti teks setiap 2 detik
    LaunchedEffect(Unit) {
        while (currentTextIndex < loadingTexts.size - 1) {
            delay(3000)
            currentTextIndex++
        }
    }

    Column(
        modifier = Modifier
            .padding(24.dp)
            .widthIn(min = 200.dp, max = 300.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = loadingTexts[currentTextIndex],
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
