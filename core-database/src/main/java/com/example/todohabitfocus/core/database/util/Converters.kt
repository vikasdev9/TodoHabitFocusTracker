package com.example.todohabitfocus.core.database.util

import androidx.room.TypeConverter
import com.example.todohabitfocus.core.domain.model.HabitFrequency
import com.example.todohabitfocus.core.domain.model.Priority

class Converters {
    @TypeConverter
    fun fromPriority(value: Priority): String = value.name

    @TypeConverter
    fun toPriority(value: String): Priority = Priority.valueOf(value)

    @TypeConverter
    fun fromHabitFrequency(value: HabitFrequency): String = value.name

    @TypeConverter
    fun toHabitFrequency(value: String): HabitFrequency = HabitFrequency.valueOf(value)
}
