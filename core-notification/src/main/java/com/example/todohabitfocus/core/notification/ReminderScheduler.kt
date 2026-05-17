package com.example.todohabitfocus.core.notification

import com.example.todohabitfocus.core.domain.model.FocusSession
import com.example.todohabitfocus.core.domain.model.Habit
import com.example.todohabitfocus.core.domain.model.Task

interface ReminderScheduler {
    fun scheduleTaskReminder(task: Task)
    fun cancelTaskReminder(taskId: String)
    
    fun scheduleHabitReminder(habit: Habit)
    fun cancelHabitReminder(habitId: String)
    
    fun scheduleFocusReminder(session: FocusSession)
    fun cancelFocusReminder()
    
    fun scheduleDailyReminder()
    fun scheduleWeeklyReminder()
}
