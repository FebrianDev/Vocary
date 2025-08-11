package com.febriandev.vocary.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.AuthRepository
import com.febriandev.vocary.domain.AppUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<AppUser?>(null)
    val user: StateFlow<AppUser?> = _user

    fun signInWithEmail(email: String, password: String, onError: (String?) -> Unit = {}) {
        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                onError(it.message)
            }
        }
    }

    fun registerWithEmail(email: String, password: String, onError: (String?) -> Unit = {}) {
        viewModelScope.launch {
            val result = authRepository.registerWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                onError(it.message)
            }
        }
    }

    fun signInWithGoogle(idToken: String, onError: (String?) -> Unit = {}) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                onError(it.message)
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }
}
