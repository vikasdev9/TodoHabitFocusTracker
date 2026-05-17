package com.example.todohabitfocus.core.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.notification.worker.DailyReminderWorker
import com.example.todohabitfocus.core.notification.worker.OverdueTaskWorker
import com.example.todohabitfocus.core.notification.worker.WeeklyReminderWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderSchedulerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ReminderScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    private val workManager = WorkManager.getInstance(context)

    override fun scheduleTaskReminder(task: Task) {
        val reminderTime = task.reminderTime ?: return
        if (reminderTime < System.currentTimeMillis()) return

        val intent = createBaseIntent(ReminderReceiver.TYPE_TASK, task.id).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, "Task Reminder")
            putExtra(ReminderReceiver.EXTRA_CONTENT, task.title)
        }

        scheduleAlarm(reminderTime, intent, task.id.hashCode())
    }

    override fun cancelTaskReminder(taskId: String) {
        cancelAlarm(createBaseIntent(ReminderReceiver.TYPE_TASK, taskId), taskId.hashCode())
    }

    override fun scheduleHabitReminder(habit: Habit) {
        val reminderTimeStr = habit.reminderTime ?: return
        val parts = reminderTimeStr.split(":")
        if (parts.size != 2) return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, parts[0].toInt())
            set(Calendar.MINUTE, parts[1].toInt())
            set(Calendar.SECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = createBaseIntent(ReminderReceiver.TYPE_HABIT, habit.id).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, "Habit Reminder")
            putExtra(ReminderReceiver.EXTRA_CONTENT, "Time for ${habit.name}!")
        }

        scheduleAlarm(calendar.timeInMillis, intent, habit.id.hashCode(), isRepeating = true)
    }

    override fun cancelHabitReminder(habitId: String) {
        cancelAlarm(createBaseIntent(ReminderReceiver.TYPE_HABIT, habitId), habitId.hashCode())
    }

    override fun scheduleFocusReminder(session: FocusSession) {
        val endTime = System.currentTimeMillis() + session.durationMillis
        val intent = createBaseIntent(ReminderReceiver.TYPE_FOCUS, session.id).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, "Focus Session Complete")
            putExtra(ReminderReceiver.EXTRA_CONTENT, "Well done! Take a break.")
        }

        scheduleAlarm(endTime, intent, NotificationConstants.FOCUS_NOTIFICATION_ID)
    }

    override fun cancelFocusReminder() {
        cancelAlarm(
            createBaseIntent(ReminderReceiver.TYPE_FOCUS, "focus_id"),
            NotificationConstants.FOCUS_NOTIFICATION_ID
        )
    }

    override fun scheduleDailyReminder() {
        val dailyWork = PeriodicWorkRequestBuilder<DailyReminderWorker>(24, TimeUnit.HOURS)
            .setInitialDelay(calculateInitialDelay(9), TimeUnit.MILLISECONDS) // 9 AM
            .build()

        workManager.enqueueUniquePeriodicWork(
            "daily_reminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            dailyWork
        )
        
        // Also schedule overdue check
        val overdueWork = PeriodicWorkRequestBuilder<OverdueTaskWorker>(6, TimeUnit.HOURS)
            .build()
            .also {
                workManager.enqueueUniquePeriodicWork(
                    "overdue_check",
                    ExistingPeriodicWorkPolicy.UPDATE,
                    it
                )
            }
    }

    override fun scheduleWeeklyReminder() {
        val weeklyWork = PeriodicWorkRequestBuilder<WeeklyReminderWorker>(7, TimeUnit.DAYS)
            .build()

        workManager.enqueueUniquePeriodicWork(
            "weekly_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            weeklyWork
        )
    }

    private fun scheduleAlarm(
        triggerAtMillis: Long,
        intent: Intent,
        requestCode: Int,
        isRepeating: Boolean = false
    ) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (alarmManager.canScheduleExactAlarms()) {
                if (isRepeating) {
                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } else {
                // Fallback to inexact
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        } else {
            if (isRepeating) {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerAtMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun cancelAlarm(intent: Intent, requestCode: Int) {
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun createBaseIntent(type: String, id: String): Intent {
        return Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TYPE, type)
            putExtra(ReminderReceiver.EXTRA_ID, id)
        }
    }

    private fun calculateInitialDelay(targetHour: Int): Long {
        val calendar = Calendar.getInstance()
        val now = calendar.timeInMillis
        calendar.set(Calendar.HOUR_OF_DAY, targetHour)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        
        if (calendar.timeInMillis <= now) {
            calendar.add(Calendar.DAY_OF_YEAR, 1)
        }
        return calendar.timeInMillis - now
    }
}
