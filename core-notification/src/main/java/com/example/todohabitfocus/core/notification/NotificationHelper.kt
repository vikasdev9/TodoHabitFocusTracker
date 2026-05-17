package com.example.todohabitfocus.core.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val notificationManager = NotificationManagerCompat.from(context)

    init {
        createNotificationChannels()
    }

    private fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val taskChannel = NotificationChannel(
                NotificationConstants.TASK_CHANNEL_ID,
                "Tasks",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for task reminders and overdue tasks"
            }

            val habitChannel = NotificationChannel(
                NotificationConstants.HABIT_CHANNEL_ID,
                "Habits",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Notifications for habit reminders"
            }

            val focusChannel = NotificationChannel(
                NotificationConstants.FOCUS_CHANNEL_ID,
                "Focus Sessions",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for focus session completion and breaks"
            }

            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannels(listOf(taskChannel, habitChannel, focusChannel))
        }
    }

    fun showTaskNotification(taskId: String, title: String, content: String) {
        if (!hasNotificationPermission()) return

        val intent = createDeepLinkIntent(NotificationConstants.DEEP_LINK_TASK_PATH, taskId)
        val pendingIntent = createPendingIntent(intent, taskId.hashCode())

        val notification = NotificationCompat.Builder(context, NotificationConstants.TASK_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Placeholder
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(taskId.hashCode(), notification)
    }

    fun showHabitNotification(habitId: String, name: String, content: String) {
        if (!hasNotificationPermission()) return

        val intent = createDeepLinkIntent(NotificationConstants.DEEP_LINK_HABIT_PATH, habitId)
        val pendingIntent = createPendingIntent(intent, habitId.hashCode())

        val notification = NotificationCompat.Builder(context, NotificationConstants.HABIT_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Placeholder
            .setContentTitle(name)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(habitId.hashCode(), notification)
    }

    fun showFocusNotification(title: String, content: String) {
        if (!hasNotificationPermission()) return

        val intent = createDeepLinkIntent(NotificationConstants.DEEP_LINK_FOCUS_PATH)
        val pendingIntent = createPendingIntent(intent, NotificationConstants.FOCUS_NOTIFICATION_ID)

        val notification = NotificationCompat.Builder(context, NotificationConstants.FOCUS_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info) // Placeholder
            .setContentTitle(title)
            .setContentText(content)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(NotificationConstants.FOCUS_NOTIFICATION_ID, notification)
    }

    private fun hasNotificationPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun createDeepLinkIntent(path: String, id: String? = null): Intent {
        val uriString = if (id != null) {
            "${NotificationConstants.DEEP_LINK_SCHEME}://$path/$id"
        } else {
            "${NotificationConstants.DEEP_LINK_SCHEME}://$path"
        }
        return Intent(Intent.ACTION_VIEW, Uri.parse(uriString))
    }

    private fun createPendingIntent(intent: Intent, requestCode: Int): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        return PendingIntent.getActivity(context, requestCode, intent, flags)
    }
}
