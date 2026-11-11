package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.Schedule

/**
 * Repository for managing focus schedules
 */
interface ScheduleRepository {
    fun getSchedules(): List<Schedule>

    fun getScheduleById(id: String): Schedule?

    fun saveSchedule(schedule: Schedule)

    fun deleteSchedule(id: String)
}
