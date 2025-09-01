package com.febriandev.vocary.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.febriandev.vocary.data.db.entity.UserEntity
import com.febriandev.vocary.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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
            repository.insertUser(user) { success, _ ->
                _insertResult.value = success
            }
            _user.value = user
        }
    }

    fun updateUser(user: UserEntity) {
        viewModelScope.launch {
            repository.updateUser(user)
            _user.value = user
        }
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