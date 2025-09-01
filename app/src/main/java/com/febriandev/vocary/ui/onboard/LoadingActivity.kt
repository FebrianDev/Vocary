package com.febriandev.vocary.ui.onboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.febriandev.vocary.BaseActivity
import com.febriandev.vocary.MainActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class LoadingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val level = intent.getStringExtra("level") ?: "Intermediate"
        val topic = intent.getStringExtra("topic") ?: "Everyday Life"

        setContent {
            VocaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    LoadingScreen(level, topic) {
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    @Composable
    fun LoadingScreen(level: String, topic: String, onNavigateToHome: () -> Unit) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("loading.json"))
        val progress by animateLottieCompositionAsState(
            composition = composition,
            iterations = LottieConstants.IterateForever
        )

        val loadingTexts = listOf(
            "Preparing your vocabulary...",
            "Connecting to AI...",
            "Generating words based on your level & topic...",
            "Getting definitions from dictionary...",
            "Almost done!"
        )

        var currentTextIndex by remember { mutableIntStateOf(0) }

        LaunchedEffect(Unit) {

            startGenerateProcess(
                topic,
                level
            )

            while (currentTextIndex <= 3) {
                delay(4000)
                currentTextIndex = (currentTextIndex + 1)
            }
        }

        LaunchedEffect(currentTextIndex) {
            if (loadingTexts.size - 1 == currentTextIndex) {
                delay(4000)
                onNavigateToHome()
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(bottom = 64.dp)
            ) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier.size(200.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = loadingTexts[currentTextIndex],
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}