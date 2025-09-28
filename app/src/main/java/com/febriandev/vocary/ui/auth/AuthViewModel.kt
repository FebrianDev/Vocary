package com.febriandev.vocary.ui.auth

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.AuthRepository
import com.febriandev.vocary.domain.AppUser
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableStateFlow<AppUser?>(null)
    val user: StateFlow<AppUser?> = _user

    private val _uiMessage = Channel<String>(Channel.BUFFERED)
    val uiMessage = _uiMessage.receiveAsFlow()

    private val _loading = MutableStateFlow<Boolean>(false)
    val loading: StateFlow<Boolean> = _loading

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = authRepository.signInWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
                _loading.value = false
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
                _loading.value = false
            }
        }
    }

    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = authRepository.registerWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
                _loading.value = false
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
                _loading.value = false
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _loading.value = true
            val result = authRepository.signInWithGoogle(idToken)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
                _loading.value = false
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
                _loading.value = false
            }
        }
    }

    suspend fun getExistUser(userId: String): Boolean {
        return authRepository.getExistUser(userId)
    }

    suspend fun signOut(gso: GoogleSignInOptions, context: Context) {
        authRepository.signOut(gso, context)
        _user.value = null
    }
}
