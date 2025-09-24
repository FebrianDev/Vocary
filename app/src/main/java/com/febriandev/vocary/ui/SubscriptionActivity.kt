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
import androidx.compose.foundation.layout.padding
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
import com.febriandev.vocary.ui.onboard.LoadingActivity
import com.febriandev.vocary.ui.theme.VocaryTheme
import com.febriandev.vocary.ui.vm.RevenueCatViewModel
import com.febriandev.vocary.ui.vm.UserViewModel
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

        setContent {
            VocaryTheme {
                var selectedPlan by remember { mutableStateOf<String?>(null) }
                var pkgPlan by remember { mutableStateOf<Package?>(null) }
                var redeemCode by remember { mutableStateOf("") }

                val offerings by revenueCatViewModel.offerings.collectAsState()
                val isPremium by revenueCatViewModel.isPremium.collectAsState()

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .windowInsetsPadding(WindowInsets.systemBars)
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Choose Your Premium Plan",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val plans = listOf(
                            "premium_1m" to "1 Month - \$4.99",
                            "premium_3m" to "3 Months - \$12.99",
                            "premium_6m" to "6 Months - \$19.99",
                            "premium_12m" to "12 Months - \$29.99 (Best Value)"
                        )

//                        plans.forEach { (id, label) ->
//
//                        }

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
                                    revenueCatViewModel.purchase(
                                        this@SubscriptionActivity,
                                        pkgPlan!!
                                    )
                                    showMessage("Processing purchase...")
                                }

                            },
                            enabled = selectedPlan != null,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Continue with Selected Plan")
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            thickness = DividerDefaults.Thickness,
                            color = Color.Gray.copy(alpha = 0.3f)
                        )

                        Text(
                            text = "Have a code?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
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
                                if (redeemCode == "p1m") {

                                    Log.d("ExpiredDate", getExpirationDate(30))

                                    val newUser = user?.copy(isPremium = true, premiumDuration = getExpirationDate(30))

                                    if (newUser != null)
                                        userViewModel.saveUser(newUser)

                                    val intent =
                                        Intent(
                                            applicationContext,
                                            LoadingActivity::class.java
                                        )

                                    //    intent.putExtra("level", level)
                                    //   intent.putExtra("topic", selectedTopic?.displayName)

                                    startActivity(intent)
                                    finish()
                                }
                            },
                            enabled = redeemCode.isNotBlank(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Redeem Code")
                        }

                        Button(
                            onClick = {
                                if (user != null)
                                    userViewModel.saveUser(user)

                                val intent =
                                    Intent(
                                        applicationContext,
                                        LoadingActivity::class.java
                                    )

                                intent.putExtra("level", user?.vocabLevel)
                                intent.putExtra("topic", user?.vocabTopic)

                                startActivity(intent)
                                finish()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Skip")
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