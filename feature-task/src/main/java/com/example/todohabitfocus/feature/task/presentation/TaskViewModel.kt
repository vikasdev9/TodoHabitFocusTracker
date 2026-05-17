package com.example.todohabitfocus.feature.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.core.domain.model.Priority
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import com.example.todohabitfocus.feature.task.domain.AddTaskUseCase
import com.example.todohabitfocus.feature.task.domain.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    fun loadTasks() {
        viewModelScope.launch {
            getTasksUseCase()
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false) }
                }
        }
    }

    fun toggleTaskCompletion(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun addTask(title: String, description: String, priority: Priority, category: String, dueDate: Long? = null) {
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                dueDate = dueDate,
                priority = priority,
                isCompleted = false,
                category = category,
                createdAt = System.currentTimeMillis()
            )
            addTaskUseCase(task)
        }
    }
    
    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }
    
    fun syncTasks() {
        viewModelScope.launch {
            try {
                taskRepository.syncTasks()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Sync failed: ${e.message}") }
            }
        }
    }
}
