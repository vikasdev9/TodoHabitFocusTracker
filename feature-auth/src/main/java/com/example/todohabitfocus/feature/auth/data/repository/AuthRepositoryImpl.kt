package com.example.todohabitfocus.feature.auth.data.repository

import com.example.todohabitfocus.feature.auth.domain.repository.AuthRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.Google
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.auth.user.UserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val supabaseClient: SupabaseClient
) : AuthRepository {

    private val _currentUser = MutableStateFlow<UserInfo?>(null)
    override val currentUser: StateFlow<UserInfo?> = _currentUser.asStateFlow()
    
    override val isLoggedIn: StateFlow<Boolean> = _currentUser
        .map { it != null }
        .stateIn(
            scope = CoroutineScope(Dispatchers.Main),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    init {
        _currentUser.value = supabaseClient.auth.currentUserOrNull()
    }

    override suspend fun signUp(email: String, password: String): Result<Unit> = runCatching {
        supabaseClient.auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    override suspend fun signIn(email: String, password: String): Result<Unit> = runCatching {
        supabaseClient.auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
        _currentUser.value = supabaseClient.auth.currentUserOrNull()
    }

    override suspend fun signInWithGoogle(): Result<Unit> = runCatching {
        supabaseClient.auth.signInWith(Google)
        _currentUser.value = supabaseClient.auth.currentUserOrNull()
    }

    override suspend fun resetPassword(email: String): Result<Unit> = runCatching {
        supabaseClient.auth.resetPasswordForEmail(email)
    }

    override suspend fun signOut(): Result<Unit> = runCatching {
        supabaseClient.auth.signOut()
        _currentUser.value = null
    }

    override suspend fun restoreSession(): Result<Boolean> = runCatching {
        val session = supabaseClient.auth.currentUserOrNull()
        _currentUser.value = session
        session != null
    }
}
