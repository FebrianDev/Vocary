package com.febriandev.vocary.ui.auth

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.febriandev.vocary.BaseActivity
import com.febriandev.vocary.R
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : BaseActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VocaryTheme {
                val context = LocalContext.current
                val user by authViewModel.user.collectAsState()

                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var isLoginMode by remember { mutableStateOf(true) }
                var errorMessage by remember { mutableStateOf<String?>(null) }

                LaunchedEffect(user) {
                    if (user != null) {
                        // onLoginSuccess()

                        Log.d("AppUser", user.toString())
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
                    Spacer(modifier = Modifier.height(24.dp))

                    Image(
                        painter = painterResource(R.drawable.icon_image2),
                        contentDescription = "",
                        modifier = Modifier.size(256.dp)
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = {
                            if (isLoginMode) {
                                authViewModel.signInWithEmail(email, password) {
                                    errorMessage = it
                                }
                            } else {
                                authViewModel.registerWithEmail(email, password) {
                                    errorMessage = it
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(if (isLoginMode) "Login" else "Register")
                    }

                    TextButton(onClick = { isLoginMode = !isLoginMode }) {
                        Text(if (isLoginMode) "Don't have an account? Register" else "Already have an account? Login")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider()

                    Spacer(modifier = Modifier.height(16.dp))

                    GoogleSignInButton(
                        onSignInResult = { idToken ->
                            authViewModel.signInWithGoogle(idToken) {
                                errorMessage = it
                            }
                        }
                    )

                    errorMessage?.let {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = it, color = MaterialTheme.colorScheme.error)
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
            .requestIdToken("YOUR_WEB_CLIENT_ID")
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(context, gso)

        Button(
            onClick = {
                launcher.launch(googleSignInClient.signInIntent)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Google")
            Spacer(Modifier.width(8.dp))
            Text("Sign in with Google")
        }
    }


}