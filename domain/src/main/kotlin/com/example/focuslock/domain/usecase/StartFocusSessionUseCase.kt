package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.model.FocusSession
import com.example.focuslock.domain.repository.FocusSessionRepository
import java.time.Instant

/**
 * Starts a new focus session
 */
class StartFocusSessionUseCase(
    private val focusSessionRepository: FocusSessionRepository,
) {
    fun start(
        id: String,
        startedAt: Instant,
        plannedDurationMinutes: Int,
        blockedApps: List<App>,
    ): FocusSession {
        val session = FocusSession(
            id = id,
            startedAt = startedAt,
            plannedDurationMinutes = plannedDurationMinutes,
            blockedApps = blockedApps,
        )
        focusSessionRepository.startSession(session)
        return session
    }
}
