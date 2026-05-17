package com.example.todohabitfocus.core.notification.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import com.example.todohabitfocus.core.notification.NotificationHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.first

@HiltWorker
class OverdueTaskWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val tasks = taskRepository.getTasks().first()
        val currentTime = System.currentTimeMillis()

        tasks.filter { !it.isCompleted }.forEach { task ->
            val dueDate = task.dueDate
            if (dueDate != null && dueDate < currentTime) {
                notificationHelper.showTaskNotification(
                    taskId = task.id,
                    title = "Task Overdue",
                    content = "Task \"${task.title}\" is overdue!"
                )
            }
        }

        return Result.success()
    }
}
