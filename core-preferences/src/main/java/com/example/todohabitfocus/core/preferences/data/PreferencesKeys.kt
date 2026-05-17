package com.example.todohabitfocus.core.preferences.data

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val IS_ONBOARDING_COMPLETED = booleanPreferencesKey("is_onboarding_completed")
    val THEME_MODE = stringPreferencesKey("theme_mode")
    val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
    val USER_NAME = stringPreferencesKey("user_name")
}
