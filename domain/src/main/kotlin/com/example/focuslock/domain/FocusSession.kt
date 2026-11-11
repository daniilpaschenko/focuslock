package com.example.focuslock.domain

import java.time.Instant

/**
 * A single focus session: a period of time spent focusing
 */
data class FocusSession(
    val id: String,
    val startedAt: Instant,
    val plannedDurationMinutes: Int,
    val endedAt: Instant? = null, // null while the session is still running
    val blockedApps: List<BlockedApp> = emptyList(),
    val status: FocusSessionStatus = FocusSessionStatus.ACTIVE,
) {
    val isOngoing: Boolean
        get() = status == FocusSessionStatus.ACTIVE && endedAt == null
}
