package com.febriandev.vocary.ui.onboard

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.work.WorkInfo
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.febriandev.vocary.BaseActivity
import com.febriandev.vocary.MainActivity
import com.febriandev.vocary.domain.AppUser
import com.febriandev.vocary.ui.theme.ThemeMode
import com.febriandev.vocary.ui.theme.ThemeState
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.utils.Constant.STEP_SCREEN
import com.febriandev.vocary.utils.Prefs
import kotlinx.coroutines.delay

class DownloadActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Prefs[STEP_SCREEN] = 3
        val user: AppUser? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("user", AppUser::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra("user")
        }

        setContent {
            VocaryTheme {
                // A surface container using the 'background' color from the theme

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

                val workerState by workerStateLiveData("DOWNLOAD_DATA").observeAsState()

                LaunchedEffect(workerState) {
                    Log.d("DownloadData", "Worker state: $workerState")

                    if (workerState == WorkInfo.State.SUCCEEDED) {
                        //delay(2000)
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }

                LaunchedEffect(Unit) {
                 //   Log.d("DownloadData", user?.uid.toString())
                    downloadData(user?.uid ?: "")
                  //  downloadData(user?.uid)
                }

                Scaffold(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.systemBars)
                        .fillMaxSize()
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(bottom = 64.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                    ) {
                        LottieAnimation(
                            composition = composition,
                            progress = { progress },
                            modifier = Modifier.size(240.dp)
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Text(
                            text = "Downloading data.....",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                    }
                }

            }

        }
    }
}