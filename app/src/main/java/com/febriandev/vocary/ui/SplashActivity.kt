package com.febriandev.vocary.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.MainActivity
import com.febriandev.vocary.ui.auth.AuthActivity
import com.febriandev.vocary.ui.auth.RegisterActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class SplashActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    SplashScreen {
                        if (userViewModel.getCurrentUser() == null) {
                            val intent = Intent(applicationContext, AuthActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SplashScreen(onSplashFinished: suspend () -> Unit) {
        LaunchedEffect(Unit) {
            delay(100)
            onSplashFinished()
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 16.dp),
        ) {

            //Logo Virtusee
//            Image(
//                painter = painterResource(id = R.drawable.splash_logo),
//                contentDescription = stringResource(R.string.vocery),
//                modifier = Modifier
//                    .padding(bottom = 128.dp)
//                    .size(196.dp)
//                    .align(Alignment.Center)
//            )

            //Logo MAS
//            Image(
//                painter = painterResource(id = R.drawable.logo_mas5),
//                contentDescription = "Logo Splash",
//                modifier = Modifier
//                    .padding(bottom = 128.dp)
//                    .size(224.dp)
//                    .align(Alignment.Center)
//            )


//            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
//                Text(
//                    text = getVersionName(),
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally),
//                    fontSize = 16.sp,
//                    color = yellowPrimary,
//                  //  fontFamily = FontFamily(Font(R.font.publicsans_medium))
//                )
//                Text(
//                    text = "V${getVersionName(applicationContext)}",
//                    modifier = Modifier
//                        .padding(bottom = 16.dp)
//                        .align(Alignment.CenterHorizontally),
//                  //  fontFamily = FontFamily(Font(R.font.publicsans_regular)),
//                    fontSize = 14.sp,
//                   // color = colorResource(id = R.color.colorSplashText)
//                )
//            }

        }
    }
}