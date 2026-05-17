package com.example.todohabitfocus.feature.task.domain

import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(task: Task) = taskRepository.insertTask(task)
}
