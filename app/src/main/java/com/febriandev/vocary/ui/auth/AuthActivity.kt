package com.febriandev.vocary.ui.auth

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.febriandev.vocary.BaseActivity
import com.febriandev.vocary.R
import com.febriandev.vocary.ui.components.EmailTextField
import com.febriandev.vocary.ui.components.PasswordTextField
import com.febriandev.vocary.ui.onboard.DownloadActivity
import com.febriandev.vocary.ui.onboard.OnboardActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.RevenueCatViewModel
import com.febriandev.vocary.utils.showMessage
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val revenueCatViewModel: RevenueCatViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                1001
            )
        }


        setContent {
            VocaryTheme {

                val user by authViewModel.user.collectAsState()
                val loading by authViewModel.loading.collectAsState()

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                var passwordVisibility by remember { mutableStateOf(false) }

                LaunchedEffect(user) {
                    if (user != null) {
                        val exists = authViewModel.getExistUser(user?.uid!!)
                        OneSignal.login(user?.uid.toString())
                        if (exists) {
                            revenueCatViewModel.logIn(user?.uid.toString())
                            val intent = Intent(applicationContext, DownloadActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        } else {
                            val intent = Intent(applicationContext, OnboardActivity::class.java)
                            intent.putExtra("user", user)
                            startActivity(intent)
                            finish()
                        }

                        Log.d("AppUser", "User exists: $exists, user data: $user")

                        Log.d("AppUser", user.toString())
                    }
                }

                LaunchedEffect(Unit) {
                    authViewModel.uiMessage.collect { message ->
                        showMessage(message)
                    }
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    containerColor = MaterialTheme.colorScheme.background
                ) { innerPadding ->

                    Column(
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.systemBars)
                            .padding(innerPadding)
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Welcome!",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Boost your vocabulary and make learning fun",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Spacer(modifier = Modifier.height(24.dp))

                        Image(
                            painter = painterResource(R.drawable.icon_vocary),
                            contentDescription = "",
                            modifier = Modifier.padding(vertical = 24.dp).size(108.dp),
                            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                        )

                        EmailTextField(
                            email = email,
                            onEmailChange = { email = it }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        PasswordTextField(
                            password = password,
                            onPasswordChange = { password = it },
                            passwordVisibility = passwordVisibility,
                            onToggleVisibility = { passwordVisibility = !passwordVisibility }
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                authViewModel.signInWithEmail(email, password)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            enabled = !loading
                        ) {
                            Text("Login")
                        }

                        TextButton(onClick = {
                            val intent = Intent(applicationContext, RegisterActivity::class.java)
                            startActivity(intent)
                        }) {
                            Text("Don't have an account? Register")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        HorizontalDivider(
                            Modifier,
                            DividerDefaults.Thickness,
                            DividerDefaults.color
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        GoogleSignInButton(
                            onSignInResult = { idToken ->
                                authViewModel.signInWithGoogle(idToken)
                            }
                        )
//                    Spacer(modifier = Modifier.height(6.dp))
//                    Button(
//                        onClick = {
//                            val intent = Intent(applicationContext, OnboardActivity::class.java)
//                            startActivity(intent)
//                        },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text("Sign in without logging in")
//                    }

                    }
                }
            }
        }
    }

    @Composable
    fun GoogleSignInButton(
        onSignInResult: (String) -> Unit
    ) {
        val context = LocalContext.current
        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                idToken?.let(onSignInResult)
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Google sign in failed", e)
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        Button(
            onClick = {
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Google")
            Spacer(Modifier.width(8.dp))
            Text("Sign in with Google")
        }
    }


}