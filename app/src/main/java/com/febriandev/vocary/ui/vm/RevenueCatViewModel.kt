package com.febriandev.vocary.ui.vm

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.revenuecat.purchases.Offerings
import com.revenuecat.purchases.Package
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import com.revenuecat.purchases.getOfferingsWith
import com.revenuecat.purchases.purchasePackageWith
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class RevenueCatViewModel @Inject constructor() : ViewModel() {

    private val _offerings = MutableStateFlow<Offerings?>(null)
    val offerings: StateFlow<Offerings?> = _offerings

    private val _isPremium = MutableStateFlow(false)
    val isPremium: StateFlow<Boolean> = _isPremium

    init {
        fetchOfferings()
        checkPremiumStatus()
    }

    private fun fetchOfferings() {
        Purchases.sharedInstance.getOfferingsWith({ error ->
            Log.e("RevenueCat", "Error fetching offerings: $error")
        }) { offerings ->
            _offerings.value = offerings
        }
    }

    private fun checkPremiumStatus() {
        Purchases.sharedInstance.getCustomerInfoWith({ error ->
            Log.e("RevenueCat", "Error getting customer info: $error")
        }) { customerInfo ->
            _isPremium.value = customerInfo.entitlements["premium"]?.isActive == true
        }
    }

    fun purchase(activity: Activity, packageToBuy: Package) {
        Purchases.sharedInstance.purchasePackageWith(
            activity, packageToBuy,
            onError = { error, _ ->
                Log.e("RevenueCat", "Purchase failed: $error")
            },
            onSuccess = { _, customerInfo ->
                _isPremium.value = customerInfo.entitlements["premium"]?.isActive == true
            }
        )
    }
}
