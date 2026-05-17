package com.example.todohabitfocus.core.notification.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todohabitfocus.core.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class DailyReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        notificationHelper.showTaskNotification(
            taskId = "daily_summary",
            title = "Daily Focus",
            content = "Ready to conquer your tasks today? Check your list!"
        )
        return Result.success()
    }
}
