package com.febriandev.vocary.ui.vm

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.logInWith
import com.revenuecat.purchases.logOutWith
import com.revenuecat.purchases.purchasePackageWith
import com.revenuecat.purchases.restorePurchasesWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

//@HiltViewModel
//class RevenueCatViewModel @Inject constructor() : ViewModel() {
//
//    private val _offerings = MutableStateFlow<Offerings?>(null)
//    val offerings: StateFlow<Offerings?> = _offerings
//
//    private val _isPremium = MutableStateFlow(false)
//    val isPremium: StateFlow<Boolean> = _isPremium
//
//    init {
//        fetchOfferings()
//        checkPremiumStatus()
//    }
//
//    private fun fetchOfferings() {
//        Purchases.sharedInstance.getOfferingsWith({ error ->
//            Log.e("RevenueCat", "Error fetching offerings: $error")
//        }) { offerings ->
//            _offerings.value = offerings
//        }
//    }
//
//    private fun checkPremiumStatus() {
//        Purchases.sharedInstance.getCustomerInfoWith({ error ->
//            Log.e("RevenueCat", "Error getting customer info: $error")
//        }) { customerInfo ->
//            _isPremium.value = customerInfo.entitlements["premium"]?.isActive == true
//        }
//    }
//
//    fun purchase(activity: Activity, packageToBuy: Package) {
//        Purchases.sharedInstance.purchasePackageWith(
//            activity, packageToBuy,
//            onError = { error, _ ->
//                Log.e("RevenueCat", "Purchase failed: $error")
//            },
//            onSuccess = { _, customerInfo ->
//                _isPremium.value = customerInfo.entitlements["premium"]?.isActive == true
//            }
//        )
//    }
//
//    fun purchase(activity: Activity, packageToBuy: Package, onSuccess: (Boolean, Date?) -> Unit) {
//        Purchases.sharedInstance.purchasePackageWith(
//            activity, packageToBuy,
//            onError = { error, _ ->
//                Log.e("RevenueCat", "Purchase failed: $error")
//                onSuccess(false, null)
//            },
//            onSuccess = { _, customerInfo ->
//                val entitlement = customerInfo.entitlements["premium"]
//                val isActive = entitlement?.isActive == true
//                val expiry = entitlement?.expirationDate
//                _isPremium.value = isActive
//                onSuccess(isActive, expiry)
//            }
//        )
//    }
//
//}

@HiltViewModel
class RevenueCatViewModel @Inject constructor() : ViewModel() {

    private val _offerings = MutableStateFlow<Offerings?>(null)
    val offerings: StateFlow<Offerings?> = _offerings

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    private val _premiumExpirationDate = MutableStateFlow<String?>(null)
    val premiumExpirationDate: StateFlow<String?> = _premiumExpirationDate

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchOfferings()
        restorePurchases()
        fetchCustomerInfo()

    }

    private fun fetchCustomerInfo() {
        Purchases.sharedInstance.getCustomerInfoWith(
            onError = { error ->
                Log.e("RevenueCat", "Failed to fetch customer info: $error")
            },
            onSuccess = { customerInfo ->
                updatePremiumStatus(customerInfo)
            }
        )
    }

    private fun fetchOfferings() {
        Purchases.sharedInstance.getOfferingsWith({ error ->
            Log.e("RevenueCat", "Error fetching offerings: $error")
        }) { offerings ->
            _offerings.value = offerings
        }
    }

    fun restorePurchases() {
        Purchases.sharedInstance.restorePurchasesWith(
            { error ->
                Log.e("RevenueCat", "Restore failed: $error")
            },
            { customerInfo ->
                updatePremiumStatus(customerInfo)
            }
        )
    }

    private fun updatePremiumStatus(customerInfo: CustomerInfo) {
        val entitlement = customerInfo.entitlements["premium"]
        val isActive = entitlement?.isActive == true
        val expiry = entitlement?.expirationDate
        _isPremium.value = isActive
        _premiumExpirationDate.value = expiry?.toInstant()?.let {
            DateTimeFormatter.ISO_INSTANT.format(it)
        }
        Log.d("RevenueCat", "Premium active=$isActive, expiry=${_premiumExpirationDate.value}")
    }

    fun purchase(
        activity: Activity,
        packageToBuy: Package,
        onActivated: () -> Unit = {},
        onTimeout: () -> Unit = {}
    ) {
        _isLoading.value = true

        Purchases.sharedInstance.purchasePackageWith(
            activity,
            packageToBuy,
            onError = { error, _ ->
                _isLoading.value = false
                if (error.code.code == 0 || error.code.code == 1) {
                    restorePurchases()
                }
            },
            onSuccess = { _, customerInfo ->
                updatePremiumStatus(customerInfo)
                if (customerInfo.entitlements["premium"]?.isActive == true) {
                    _isLoading.value = false
                    onActivated()
                } else {
                    // jalankan auto-polling
                    waitForPremiumActivation(
                        onActivated = {
                            _isLoading.value = false
                            onActivated()
                        },
                        onTimeout = {
                            _isLoading.value = false
                            onTimeout()
                        }
                    )
                }
            }
        )
    }

    private fun waitForPremiumActivation(
        intervalMillis: Long = 5000L,
        timeoutMillis: Long = 60000L,
        onActivated: () -> Unit,
        onTimeout: () -> Unit
    ) {
        viewModelScope.launch {
            val start = System.currentTimeMillis()
            var activated = false
            while (System.currentTimeMillis() - start < timeoutMillis && !activated) {
                Purchases.sharedInstance.getCustomerInfoWith(
                    onError = { error ->
                        Log.e("RevenueCat", "Polling check failed: $error")
                    },
                    onSuccess = { customerInfo ->
                        val isActive = customerInfo.entitlements["premium"]?.isActive == true
                        if (isActive) {
                            updatePremiumStatus(customerInfo)
                            activated = true
                            onActivated()
                        }
                    }
                )
                if (!activated) delay(intervalMillis)
            }
            if (!activated) onTimeout()
        }
    }

    fun logIn(userId: String, onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        _isLoading.value = true

        Purchases.sharedInstance.logInWith(
            userId,
            onError = { error ->
                _isLoading.value = false
                Log.e("RevenueCat", "logIn error: $error")
                onError(error.message)
            },
            onSuccess = { customerInfo, _ ->
                _isLoading.value = false
                updatePremiumStatus(customerInfo)
                onSuccess()
            }
        )
    }

    // --- LOGOUT ---
    fun logOut(onSuccess: () -> Unit = {}, onError: (String) -> Unit = {}) {
        _isLoading.value = true

        Purchases.sharedInstance.logOutWith(
            onError = { error ->
                _isLoading.value = false
                Log.e("RevenueCat", "logOut error: $error")
                onError(error.message)
            },
            onSuccess = { customerInfo ->
                _isLoading.value = false
                // Setelah logout, status kembali anonymous → entitlements kosong
                updatePremiumStatus(customerInfo)
                onSuccess()
            }
        )
    }

//    private fun getDefaultDuration(pkg: Package): String {
//        val now = Instant.now()
//        val expiryInstant = when (pkg.product.id) {
//            "premium_1m:premium-1m" -> now.plus(30, ChronoUnit.DAYS)
//            "premium_3m:premium-3m" -> now.plus(90, ChronoUnit.DAYS)
//            "premium_6m:premium-6m" -> now.plus(180, ChronoUnit.DAYS)
//            "premium_12m:premium-12m" -> now.plus(365, ChronoUnit.DAYS)
//            else -> now.plus(0, ChronoUnit.DAYS)
//        }
//
//        return DateTimeFormatter.ISO_INSTANT.format(expiryInstant)
//    }
}
