package com.example.todohabitfocus.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.feature.auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, emailError = null) }
    }

    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, passwordError = null) }
    }

    fun onConfirmPasswordChange(password: String) {
        _uiState.update { it.copy(confirmPassword = password, confirmPasswordError = null) }
    }

    fun login() {
        if (!validateLogin()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.signIn(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun register() {
        if (!validateRegister()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.signUp(_uiState.value.email, _uiState.value.password)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun forgotPassword() {
        if (_uiState.value.email.isBlank()) {
            _uiState.update { it.copy(emailError = "Email cannot be empty") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.resetPassword(_uiState.value.email)
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    fun loginWithGoogle() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            repository.signInWithGoogle()
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(isLoading = false, error = error.message) }
                }
        }
    }

    private fun validateLogin(): Boolean {
        val emailValid = _uiState.value.email.isNotBlank()
        val passwordValid = _uiState.value.password.isNotBlank()

        if (!emailValid) _uiState.update { it.copy(emailError = "Email cannot be empty") }
        if (!passwordValid) _uiState.update { it.copy(passwordError = "Password cannot be empty") }

        return emailValid && passwordValid
    }

    private fun validateRegister(): Boolean {
        val emailValid = _uiState.value.email.isNotBlank()
        val passwordValid = _uiState.value.password.length >= 6
        val passwordsMatch = _uiState.value.password == _uiState.value.confirmPassword

        if (!emailValid) _uiState.update { it.copy(emailError = "Email cannot be empty") }
        if (!passwordValid) _uiState.update { it.copy(passwordError = "Password must be at least 6 characters") }
        if (!passwordsMatch) _uiState.update { it.copy(confirmPasswordError = "Passwords do not match") }

        return emailValid && passwordValid && passwordsMatch
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
