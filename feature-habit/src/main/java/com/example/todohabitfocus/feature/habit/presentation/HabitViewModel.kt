package com.example.todohabitfocus.feature.habit.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.HabitFrequency
import com.example.todohabitfocus.core.domain.model.HabitLog
import com.example.todohabitfocus.core.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class HabitUiState(
    val habits: List<HabitItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class HabitItemState(
    val habit: Habit,
    val logs: List<HabitLog> = emptyList(),
    val currentStreak: Int = 0,
    val isCompletedToday: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class HabitViewModel @Inject constructor(
    private val habitRepository: HabitRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HabitUiState(isLoading = true))
    val uiState: StateFlow<HabitUiState> = _uiState.asStateFlow()

    init {
        loadHabits()
    }

    fun loadHabits() {
        viewModelScope.launch {
            habitRepository.getHabits()
                .flatMapLatest { habits ->
                    val habitStates = habits.map { habit ->
                        habitRepository.getHabitLogs(habit.id).map { logs ->
                            val sortedLogs = logs.sortedByDescending { it.date }
                            HabitItemState(
                                habit = habit,
                                logs = logs,
                                currentStreak = calculateStreak(sortedLogs),
                                isCompletedToday = isToday(sortedLogs.firstOrNull()?.date)
                            )
                        }
                    }
                    if (habitStates.isEmpty()) flowOf(emptyList())
                    else combine(habitStates) { it.toList() }
                }
                .onStart { _uiState.update { it.copy(isLoading = true) } }
                .catch { e -> _uiState.update { it.copy(isLoading = false, error = e.message) } }
                .collect { habits ->
                    _uiState.update { it.copy(habits = habits, isLoading = false) }
                }
        }
    }

    fun toggleHabit(habitId: String) {
        viewModelScope.launch {
            val today = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val log = HabitLog(
                id = UUID.randomUUID().toString(),
                habitId = habitId,
                date = today,
                isCompleted = true
            )
            habitRepository.insertHabitLog(log)
        }
    }

    fun addHabit(name: String, description: String, frequency: HabitFrequency) {
        viewModelScope.launch {
            val habit = Habit(
                id = UUID.randomUUID().toString(),
                name = name,
                description = description,
                frequency = frequency,
                reminderTime = null
            )
            habitRepository.insertHabit(habit)
        }
    }

    private fun calculateStreak(logs: List<HabitLog>): Int {
        if (logs.isEmpty()) return 0
        var streak = 0
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        var checkDate = calendar.timeInMillis

        for (log in logs) {
            if (log.date == checkDate && log.isCompleted) {
                streak++
                calendar.add(Calendar.DAY_OF_YEAR, -1)
                checkDate = calendar.timeInMillis
            } else if (log.date < checkDate) {
                if (streak == 0) {
                     calendar.add(Calendar.DAY_OF_YEAR, -1)
                     checkDate = calendar.timeInMillis
                     if (log.date == checkDate && log.isCompleted) {
                         streak++
                         calendar.add(Calendar.DAY_OF_YEAR, -1)
                         checkDate = calendar.timeInMillis
                         continue
                     }
                }
                break
            }
        }
        return streak
    }

    private fun isToday(date: Long?): Boolean {
        if (date == null) return false
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
        return date == today
    }
}
