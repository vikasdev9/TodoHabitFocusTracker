package com.example.todohabitfocus.core.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ReminderReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val type = intent.getStringExtra(EXTRA_TYPE)
        val id = intent.getStringExtra(EXTRA_ID)
        val title = intent.getStringExtra(EXTRA_TITLE) ?: "Reminder"
        val content = intent.getStringExtra(EXTRA_CONTENT) ?: "You have a new reminder"

        when (type) {
            TYPE_TASK -> {
                id?.let { notificationHelper.showTaskNotification(it, title, content) }
            }
            TYPE_HABIT -> {
                id?.let { notificationHelper.showHabitNotification(it, title, content) }
            }
            TYPE_FOCUS -> {
                notificationHelper.showFocusNotification(title, content)
            }
        }
    }

    companion object {
        const val EXTRA_TYPE = "extra_type"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_CONTENT = "extra_content"

        const val TYPE_TASK = "task"
        const val TYPE_HABIT = "habit"
        const val TYPE_FOCUS = "focus"
    }
}
