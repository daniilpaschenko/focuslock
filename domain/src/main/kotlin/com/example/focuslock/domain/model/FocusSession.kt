package com.example.focuslock.domain.model

import java.time.Instant

/**
 * A single focus session: a period of time spent focusing.
 *
 * @param id unique session identifier.
 * @param startedAt when the session has started.
 * @param plannedDurationMinutes planned focus duration in minutes.
 * @param endedAt when the session has ended, null while the session is still running.
 * @param blockedApps applications that had to be blocked during the session.
 * @param status current lifecycle state of the session.
 */
data class FocusSession(
    val id: String,
    val startedAt: Instant,
    val plannedDurationMinutes: Int,
    val endedAt: Instant? = null,
    val blockedApps: List<App> = emptyList(),
    val status: FocusSessionStatus = FocusSessionStatus.ACTIVE,
) {
    val isOngoing: Boolean
        get() = status == FocusSessionStatus.ACTIVE && endedAt == null
}
