package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository

/**
 * Toggles the blocked state of an application
 */
class ToggleBlockedAppUseCase(
    private val blockedAppsRepository: BlockedAppsRepository,
) {
    fun toggleBlockedApp(app: App): Boolean {
        val isBlocked = blockedAppsRepository.getBlockedApps()
            .any { it.packageName == app.packageName }

        return if (isBlocked) {
            blockedAppsRepository.removeBlockedApp(app.packageName)
            false
        } else {
            blockedAppsRepository.addBlockedApp(app)
            true
        }
    }
}
