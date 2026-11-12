package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import javax.inject.Inject

/**
 * Toggles the blocked state of an application
 *
 * @return true if the app is blocked after the call, false if it was unblocked
 */
class ToggleBlockedAppUseCase @Inject constructor(
    private val blockedAppsRepository: BlockedAppsRepository,
) {
    suspend fun toggleBlockedApp(app: App): Boolean {
        val isBlocked = blockedAppsRepository.isAppBlocked(app.packageName)

        return if (isBlocked) {
            blockedAppsRepository.removeBlockedApp(app.packageName)
            false
        } else {
            blockedAppsRepository.addBlockedApp(app)
            true
        }
    }
}