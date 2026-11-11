package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.model.FocusSession

/**
 * Decides whether the given app should be blocked during the session
 */

class ShouldBlockAppUseCase {
    fun shouldBlock(app: App, session: FocusSession): Boolean =
        session.blockedApps.any { it.packageName == app.packageName }
}
