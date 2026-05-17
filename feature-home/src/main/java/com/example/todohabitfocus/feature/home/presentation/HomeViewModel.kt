package com.example.todohabitfocus.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.core.domain.model.Task
import com.example.todohabitfocus.core.domain.repository.FocusRepository
import com.example.todohabitfocus.core.domain.repository.HabitRepository
import com.example.todohabitfocus.core.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskRepository,
    private val habitRepository: HabitRepository,
    private val focusRepository: FocusRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadDashboardData()
    }

    fun loadDashboardData() {
        viewModelScope.launch {
            combine(
                taskRepository.getTasks(),
                habitRepository.getHabits(),
                focusRepository.getFocusSessions()
            ) { tasks, habits, sessions ->
                
                val upcomingTasks = tasks.filter { !it.isCompleted }
                    .sortedBy { it.dueDate ?: Long.MAX_VALUE }
                    .take(3)

                val habitStreaks = habits.map { habit ->
                    // Simplified streak calculation for demo
                    HabitStreak(habit, streakCount = (3..12).random(), isCompletedToday = Random().nextBoolean())
                }

                val totalFocusTime = sessions.sumOf { it.durationMillis }
                val focusStats = FocusStats(
                    totalFocusTimeMinutes = totalFocusTime / (1000 * 60),
                    sessionsCompleted = sessions.size,
                    dailyGoalProgress = (totalFocusTime.toFloat() / (1000 * 60 * 120)).coerceAtMost(1f) // 2 hour goal
                )

                val productivityScore = calculateProductivityScore(tasks, habits, sessions)

                val weeklyActivity = generateMockWeeklyActivity()

                HomeUiState(
                    isLoading = false,
                    upcomingTasks = upcomingTasks,
                    habitStreaks = habitStreaks,
                    focusStats = focusStats,
                    productivityScore = productivityScore,
                    weeklyActivity = weeklyActivity
                )
            }.onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.catch { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    private fun calculateProductivityScore(tasks: List<Task>, habits: List<Any>, sessions: List<Any>): Int {
        val taskFactor = if (tasks.isNotEmpty()) (tasks.count { it.isCompleted }.toFloat() / tasks.size * 40).toInt() else 0
        val habitFactor = 30 // Mocked
        val focusFactor = 25 // Mocked
        return (taskFactor + habitFactor + focusFactor).coerceIn(0, 100)
    }

    private fun generateMockWeeklyActivity(): List<ActivityData> {
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return days.map { day ->
            ActivityData(
                dayName = day,
                taskCount = (2..8).random(),
                focusHours = (1..5).random().toFloat() + Random().nextFloat()
            )
        }
    }
}
