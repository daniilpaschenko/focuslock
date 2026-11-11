package com.example.focuslock.domain

import java.time.DayOfWeek
import java.time.LocalTime

/**
 * Recurring focus plan: at what time and how long to focus,
 * which apps to block and on which days of the week it repeats
 */
data class Schedule(
    val id: String,
    val name: String,
    val startTime: LocalTime,
    val durationMinutes: Int,
    val daysOfWeek: List<DayOfWeek>,
    val blockedApps: List<BlockedApp> = emptyList(),
    val isEnabled: Boolean = true,
)
