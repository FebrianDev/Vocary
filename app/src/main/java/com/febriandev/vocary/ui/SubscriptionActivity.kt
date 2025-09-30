package com.febriandev.vocary.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.ui.components.TitleTopBar
import com.febriandev.vocary.ui.onboard.LoadingActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.RevenueCatViewModel
import com.febriandev.vocary.ui.vm.UserViewModel
import com.febriandev.vocary.utils.Constant.STEP_SCREEN
import com.febriandev.vocary.utils.Prefs
import com.febriandev.vocary.utils.getExpirationDate
import com.febriandev.vocary.utils.showMessage
import com.revenuecat.purchases.Package
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class SubscriptionActivity : ComponentActivity() {

    private val revenueCatViewModel: RevenueCatViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val user = intent.getParcelableExtra<UserEntity>("user")
        val isSetting = intent.getBooleanExtra("isSetting", false)

        if (!isSetting) Prefs[STEP_SCREEN] = 2

        setContent {
            VocaryTheme {
                var selectedPlan by remember { mutableStateOf<String?>(null) }
                var pkgPlan by remember { mutableStateOf<Package?>(null) }
                var redeemCode by remember { mutableStateOf("") }

                val offerings by revenueCatViewModel.offerings.collectAsState()
                val premium by revenueCatViewModel.isPremium.collectAsState()
                val premiumExpirationDate by revenueCatViewModel.premiumExpirationDate.collectAsState()

                val isLoading by revenueCatViewModel.isLoading.collectAsState()

                LaunchedEffect(premium, premiumExpirationDate) {

                    Log.d("Premium",premiumExpirationDate.toString())

                    if (premium && user != null) {

                        val newUser = user.copy(
                            premium = true,
                            premiumDuration = premiumExpirationDate,
                            isRevenueCat = true
                        )
                        userViewModel.saveUser(newUser)
                        showMessage("Premium activated!")

                        if (!isSetting) {
                            val intent =
                                Intent(
                                    applicationContext,
                                    LoadingActivity::class.java
                                )
                                    .apply {
                                        putExtra(
                                            "level",
                                            user.vocabLevel ?: "Intermediate"
                                        )
                                        putExtra(
                                            "topic",
                                            user.vocabTopic ?: "Everyday Life"
                                        )
                                        putExtra("userId", user.id)
                                    }
                            startActivity(intent)
                            finish()
                        }

                    }
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
                            .padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {


                        if (isSetting) {
                            TitleTopBar("Subscription") { finish() }
                        } else {
                            Text(
                                text = "Choose Your Premium Plan",
                                // fontSize = 22.sp,
                                style = MaterialTheme.typography.headlineMedium,
                            )
                        }

                        when {
                            offerings == null -> {
                                Box(
                                    modifier = Modifier.wrapContentSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            offerings?.all?.get("default")?.availablePackages.isNullOrEmpty() -> {
                                // offering kosong
                                Text(
                                    "No packages available. Check the RevenueCat dashboard.",
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

                            else -> {
                                offerings?.all?.get("default")?.availablePackages?.forEach { pkg ->
                                    Log.d(
                                        "RevenueCat",
                                        "Available package id=${pkg.product.id}, title=${pkg.product.title}"
                                    )
                                    SubscriptionOption(
                                        text = pkg.product.title,
                                        isSelected = selectedPlan == pkg.product.id,
                                        onClick = {
                                            selectedPlan = pkg.product.id
                                            pkgPlan = pkg
                                        }
                                    )
                                }
                            }
                        }

                        Button(
                            onClick = {
                                if (pkgPlan != null) {
                                    showMessage("Processing purchase...")
                                    revenueCatViewModel.purchase(
                                        this@SubscriptionActivity,
                                        pkgPlan!!,
                                        onActivated = {

                                        },
                                        onTimeout = {
                                            showMessage("Activation is taking longer than expected. Please try again or restore purchases.")
                                        }
                                    )
                                }

                            },
                            enabled = selectedPlan != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Continue with Selected Plan")
                            }
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = DividerDefaults.Thickness,
                            color = Color.Gray.copy(alpha = 0.3f)
                        )

                        Text(
                            text = "Have a code?",
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                        )

                        OutlinedTextField(
                            value = redeemCode,
                            onValueChange = { redeemCode = it },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("Enter access code") },
                            singleLine = true,
                            shape = RoundedCornerShape(24.dp)
                        )

                        Button(
                            onClick = {

                                if (user?.premium == true) {
                                    showMessage("You're already premium")
                                    return@Button
                                }

                                if (redeemCode == "p1m") {

                                    Log.d("ExpiredDate", getExpirationDate(30))

                                    val newUser = user?.copy(
                                        premium = true,
                                        premiumDuration = getExpirationDate(30),
                                        isRevenueCat = false
                                    )

                                    if (newUser != null)
                                        userViewModel.saveUser(newUser)

                                    if (isSetting) {
                                        finish()
                                    } else {
                                        val intent =
                                            Intent(
                                                applicationContext,
                                                LoadingActivity::class.java
                                            )

                                        intent.putExtra("level", user?.vocabLevel ?: "Intermediate")
                                        intent.putExtra(
                                            "topic",
                                            user?.vocabTopic ?: "Everyday Life"
                                        )
                                        intent.putExtra("userId", user?.id ?: "")

                                        startActivity(intent)
                                        finish()
                                    }
                                } else {
                                    showMessage("Wrong code")
                                }
                            },
                            enabled = redeemCode.isNotBlank(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Text("Redeem Code")
                        }

                        if (!isSetting) {
                            Button(
                                onClick = {
                                    if (user != null)
                                        userViewModel.saveUser(user)

                                    val intent =
                                        Intent(
                                            applicationContext,
                                            LoadingActivity::class.java
                                        )

                                    intent.putExtra("level", user?.vocabLevel ?: "Intermediate")
                                    intent.putExtra("topic", user?.vocabTopic ?: "Everyday Life")
                                    intent.putExtra("userId", user?.id ?: "")

                                    startActivity(intent)
                                    finish()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Skip")
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun SubscriptionOption(
        text: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            tonalElevation = if (isSelected) 4.dp else 1.dp,
            color = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            else MaterialTheme.colorScheme.surface
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    fontSize = 16.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

}