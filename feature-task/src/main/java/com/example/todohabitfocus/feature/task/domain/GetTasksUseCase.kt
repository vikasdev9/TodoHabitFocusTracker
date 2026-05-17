package com.example.todohabitfocus.feature.task.domain

import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = taskRepository.getTasks()
}
