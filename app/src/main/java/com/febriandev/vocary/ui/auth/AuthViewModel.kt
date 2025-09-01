package com.febriandev.vocary.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.repository.AuthRepository
import com.febriandev.vocary.domain.AppUser
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

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
            }
        }
    }

    fun registerWithEmail(email: String, password: String) {
        viewModelScope.launch {
            val result = authRepository.registerWithEmail(email, password)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            val result = authRepository.signInWithGoogle(idToken)
            result.onSuccess {
                _user.value = AppUser(it.uid, it.displayName, it.email)
            }.onFailure {
                _uiMessage.send(it.message ?: "Something was wrong!")
            }
        }
    }

    fun signOut() {
        authRepository.signOut()
        _user.value = null
    }
}
