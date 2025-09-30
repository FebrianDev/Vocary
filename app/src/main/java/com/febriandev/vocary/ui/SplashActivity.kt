package com.febriandev.vocary.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febriandev.vocary.R
import com.febriandev.vocary.ui.auth.AuthActivity
import com.febriandev.vocary.ui.onboard.DownloadActivity
import com.febriandev.vocary.ui.onboard.OnboardActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.utils.Constant.STEP_SCREEN
import com.febriandev.vocary.utils.Prefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    // private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val step = Prefs[STEP_SCREEN, 0]

        setContent {
            VocaryTheme {
                SplashScreen {
                    when (step) {
                        0 -> {
                            val intent = Intent(applicationContext, AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        1 -> {
                            val intent = Intent(applicationContext, OnboardActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        2 -> {
                            val intent =
                                Intent(applicationContext, SubscriptionActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                        3 -> {
                            val intent = Intent(applicationContext, DownloadActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun SplashScreen(onSplashFinished: suspend () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onSplashFinished()
    }

    Scaffold(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.systemBars)
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {

            Image(
                painter = painterResource(R.drawable.icon_vocary),
                contentDescription = stringResource(R.string.app_name),
                modifier = Modifier
                    .padding(bottom = 224.dp)
                    .size(124.dp)
                    .align(Alignment.Center),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Column(
                modifier = Modifier
                    .padding(bottom = 0.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(
                    text = "Built by febriandev",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    //  fontFamily = FontFamily(Font(R.font.publicsans_medium))
                )
                Text(
                    text = "v1.0.1",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    //  fontFamily = FontFamily(Font(R.font.publicsans_regular)),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

            }

        }

    }
}