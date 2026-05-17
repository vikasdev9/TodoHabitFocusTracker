package com.example.todohabitfocus.core.preferences.data

import com.example.todohabitfocus.core.preferences.domain.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : PreferencesRepository {

    override fun getThemeMode(): Flow<String> = 
        dataStoreManager.getPreference(PreferencesKeys.THEME_MODE, "light")

    override suspend fun setThemeMode(mode: String) {
        dataStoreManager.setPreference(PreferencesKeys.THEME_MODE, mode)
    }

    override fun isOnboardingCompleted(): Flow<Boolean> = 
        dataStoreManager.getPreference(PreferencesKeys.IS_ONBOARDING_COMPLETED, false)

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStoreManager.setPreference(PreferencesKeys.IS_ONBOARDING_COMPLETED, completed)
    }

    override fun areNotificationsEnabled(): Flow<Boolean> = 
        dataStoreManager.getPreference(PreferencesKeys.NOTIFICATIONS_ENABLED, true)

    override suspend fun setNotificationsEnabled(enabled: Boolean) {
        dataStoreManager.setPreference(PreferencesKeys.NOTIFICATIONS_ENABLED, enabled)
    }

    override fun getUserName(): Flow<String?> = 
        dataStoreManager.getNullablePreference(PreferencesKeys.USER_NAME)

    override suspend fun setUserName(name: String) {
        dataStoreManager.setPreference(PreferencesKeys.USER_NAME, name)
    }
}
