package com.example.todohabitfocus.core.preferences.domain

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {
    fun getThemeMode(): Flow<String>
    suspend fun setThemeMode(mode: String)
    
    fun isOnboardingCompleted(): Flow<Boolean>
    suspend fun setOnboardingCompleted(completed: Boolean)
    
    fun areNotificationsEnabled(): Flow<Boolean>
    suspend fun setNotificationsEnabled(enabled: Boolean)

    fun getUserName(): Flow<String?>
    suspend fun setUserName(name: String)
}
