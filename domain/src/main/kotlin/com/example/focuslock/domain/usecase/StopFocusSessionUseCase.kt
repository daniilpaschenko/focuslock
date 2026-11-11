package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.repository.FocusSessionRepository
import java.time.Instant

/**
 * Stops an active focus session
 */

class StopFocusSessionUseCase(
    private val focusSessionRepository: FocusSessionRepository,
) {
    fun stop(sessionId: String, endedAt: Instant) {
        focusSessionRepository.completeSession(sessionId = sessionId, endedAt = endedAt)
    }
}
