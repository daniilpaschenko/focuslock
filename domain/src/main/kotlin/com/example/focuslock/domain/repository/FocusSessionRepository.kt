package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.FocusSession
import java.time.Instant

/**
 * Repository for storing and retrieving focus sessions
 */
interface FocusSessionRepository {
    fun getActiveSession(): FocusSession?

    fun getSessions(): List<FocusSession>

    fun startSession(session: FocusSession)

    fun completeSession(sessionId: String, endedAt: Instant)
}
