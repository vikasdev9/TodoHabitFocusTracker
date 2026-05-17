package com.example.todohabitfocus.core.notification.permission

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationPermissionRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(
        "notification_permissions_prefs",
        Context.MODE_PRIVATE
    )

    fun isPermissionRequested(): Boolean {
        return sharedPreferences.getBoolean(KEY_PERMISSION_REQUESTED, false)
    }

    fun setPermissionRequested(requested: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_PERMISSION_REQUESTED, requested).apply()
    }

    companion object {
        private const val KEY_PERMISSION_REQUESTED = "key_notification_permission_requested"
    }
}
