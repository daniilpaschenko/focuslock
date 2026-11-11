package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.model.FocusSession
import com.example.focuslock.domain.model.FocusSessionStatus
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Instant

class ShouldBlockAppUseCaseTest {

    private val shouldBlockAppUseCase = ShouldBlockAppUseCase()

    private fun focusSession(vararg blockedPackageNames: String): FocusSession = FocusSession(
        id = "session-1",
        startedAt = Instant.parse("2026-11-11T09:00:00Z"),
        plannedDurationMinutes = 25,
        endedAt = null,
        blockedApps = blockedPackageNames.map { App(it, "App $it") },
        status = FocusSessionStatus.ACTIVE,
    )

    @Test
    fun shouldBlockAppReturnsTrueWhenAppIsInSessionBlockedList() {
        val session = focusSession("com.example.focuslock.app", "com.android.chrome.browser")

        val result = shouldBlockAppUseCase.shouldBlock(
            app = App(packageName = "com.android.chrome.browser", appName = "Chrome"),
            session = session,
        )

        assertTrue(result)
    }

    @Test
    fun shouldBlockAppReturnsFalseWhenAppIsNotInSessionBlockedList() {
        val session = focusSession("com.example.focuslock.app")

        val result = shouldBlockAppUseCase.shouldBlock(
            app = App(packageName = "com.android.chrome.browser", appName = "Chrome"),
            session = session,
        )

        assertFalse(result)
    }

    @Test
    fun shouldBlockAppReturnsFalseWhenSessionHasNoBlockedApps() {
        val session = focusSession()

        val result = shouldBlockAppUseCase.shouldBlock(
            app = App(packageName = "com.android.chrome.browser", appName = "Chrome"),
            session = session,
        )

        assertFalse(result)
    }
}
