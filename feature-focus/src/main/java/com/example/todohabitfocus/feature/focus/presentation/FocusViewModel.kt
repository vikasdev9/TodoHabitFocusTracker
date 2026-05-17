package com.example.todohabitfocus.feature.focus.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.core.domain.model.SessionType
import com.example.todohabitfocus.core.domain.repository.FocusRepository
import com.example.todohabitfocus.feature.focus.timer.FocusTimerManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

data class FocusUiState(
    val timeLeftMillis: Long = 0,
    val isRunning: Boolean = false,
    val currentType: SessionType = SessionType.POMODORO,
    val totalFocusTime: Long = 0,
    val sessions: List<FocusSession> = emptyList()
)

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val focusRepository: FocusRepository,
    private val timerManager: FocusTimerManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusUiState())
    val uiState: StateFlow<FocusUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                timerManager.timeLeft,
                timerManager.isRunning,
                focusRepository.getTotalFocusTime(),
                focusRepository.getFocusSessions()
            ) { timeLeft, isRunning, totalTime, sessions ->
                FocusUiState(
                    timeLeftMillis = timeLeft,
                    isRunning = isRunning,
                    totalFocusTime = totalTime,
                    sessions = sessions
                )
            }.collect { state ->
                _uiState.value = state
            }
        }
    }

    fun startPomodoro() {
        val duration = 25 * 60 * 1000L
        timerManager.startTimer(duration) {
            saveSession(duration, SessionType.POMODORO)
        }
    }

    fun startShortBreak() {
        val duration = 5 * 60 * 1000L
        timerManager.startTimer(duration) {
            saveSession(duration, SessionType.SHORT_BREAK)
        }
    }

    fun stopTimer() {
        timerManager.stopTimer()
    }

    private fun saveSession(duration: Long, type: SessionType) {
        viewModelScope.launch {
            val session = FocusSession(
                id = UUID.randomUUID().toString(),
                startTime = System.currentTimeMillis(),
                durationMillis = duration,
                type = type
            )
            focusRepository.insertFocusSession(session)
        }
    }
}
