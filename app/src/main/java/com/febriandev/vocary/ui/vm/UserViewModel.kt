package com.febriandev.vocary.ui.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.repository.UserRepository
import com.revenuecat.purchases.CacheFetchPolicy
import com.revenuecat.purchases.CustomerInfo
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.PurchasesError
import com.revenuecat.purchases.interfaces.ReceiveCustomerInfoCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _insertResult = MutableStateFlow(false)
    val insertResult: StateFlow<Boolean> = _insertResult

    suspend fun getCurrentUser(): UserEntity? {
        return repository.getCurrentUser()
    }

    fun getUser() = viewModelScope.launch {
        _user.value = repository.getCurrentUser()
    }

    fun saveUser(user: UserEntity) {
        viewModelScope.launch {
            repository.insertOrUpdateUser(user)
//            repository.insertUser(user) { success, _ ->
//                _insertResult.value = success
//            }
            _user.value = user
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
            _user.value = user
        }
    }

    fun updateUserPremium(id: String, premium: Boolean, premiumDuration: String?) {
        viewModelScope.launch {
            repository.updatePremiumFields(id, premium, premiumDuration)
            //_user.value = user
        }
    }


    fun syncPremiumStatus(user: UserEntity) {
        Purchases.sharedInstance.getCustomerInfo(
            CacheFetchPolicy.FETCH_CURRENT,
            object : ReceiveCustomerInfoCallback {
                override fun onReceived(customerInfo: CustomerInfo) {
                    try {
                        val entitlement = customerInfo.entitlements.active["premium"]
                        val isActiveFromRC = entitlement?.isActive == true
                        val rcExpiryInstant = entitlement?.expirationDate?.toInstant()
                        val rcExpiryString = rcExpiryInstant?.toString()

                        val now = Instant.now()
                        val localExpiryInstant = runCatching {
                            user.premiumDuration?.let { Instant.parse(it) }
                        }.getOrNull()
                        val localExpired = localExpiryInstant?.isBefore(now) ?: true

                        when {
                            // 1) Lokal aktif tapi RC bilang tidak aktif
                            user.premium && !isActiveFromRC -> {
                                updateUserPremium(user.id, false, null)
                            }

                            // 2) Lokal expired
                            user.premium && localExpired -> {
                                updateUserPremium(user.id, false, null)
                            }

                            // 3) RC aktif & belum expired
                            isActiveFromRC && (rcExpiryInstant == null || rcExpiryInstant.isAfter(now)) -> {
                                if (!user.premium || user.premiumDuration != rcExpiryString) {
                                    updateUserPremium(user.id, true, rcExpiryString)
                                }
                            }

                            // 4) else → sudah konsisten
                        }
                    } catch (e: Exception) {
                        Log.e("RevenueCat", "syncPremiumStatus: parse/logic error", e)
                    }
                }

                override fun onError(error: PurchasesError) {
                    Log.e("RevenueCat", "getCustomerInfo error: $error")
                }
            }
        )
    }


    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            repository.deleteUser(user)
            _user.value = null
        }
    }

    fun clearUsers() {
        viewModelScope.launch {
            repository.clearUsers()
            _user.value = null
        }
    }
}