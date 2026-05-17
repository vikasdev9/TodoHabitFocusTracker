package com.example.todohabitfocus.presentation.appstart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.core.preferences.domain.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AppStartUiState {
    data object Loading : AppStartUiState
    data class Nav(
        val onboardingCompleted: Boolean
    ) : AppStartUiState
}

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<AppStartUiState>(AppStartUiState.Loading)
    val uiState: StateFlow<AppStartUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            // Minimum splash time for branding
            val splashJob = launch { delay(2000) }
            
            // Read onboarding state
            val onboardingCompleted = preferencesRepository.isOnboardingCompleted().first()
            
            splashJob.join()
            
            _uiState.value = AppStartUiState.Nav(
                onboardingCompleted = onboardingCompleted
            )
        }
    }
}
