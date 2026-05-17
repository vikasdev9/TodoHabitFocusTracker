package com.example.todohabitfocus.feature.focus.timer

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FocusTimerManager @Inject constructor() {
    private val _timeLeft = MutableStateFlow(0L)
    val timeLeft = _timeLeft.asStateFlow()

    private val _isRunning = MutableStateFlow(false)
    val isRunning = _isRunning.asStateFlow()

    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun startTimer(durationMillis: Long, onFinish: () -> Unit) {
        _timeLeft.value = durationMillis
        _isRunning.value = true
        timerJob?.cancel()
        timerJob = scope.launch {
            while (_timeLeft.value > 0) {
                delay(1000)
                _timeLeft.value -= 1000
            }
            _isRunning.value = false
            onFinish()
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        _isRunning.value = false
        _timeLeft.value = 0
    }

    fun pauseTimer() {
        timerJob?.cancel()
        _isRunning.value = false
    }

    fun resumeTimer(onFinish: () -> Unit) {
        if (_timeLeft.value > 0) {
            _isRunning.value = true
            timerJob = scope.launch {
                while (_timeLeft.value > 0) {
                    delay(1000)
                    _timeLeft.value -= 1000
                }
                _isRunning.value = false
                onFinish()
            }
        }
    }
}
