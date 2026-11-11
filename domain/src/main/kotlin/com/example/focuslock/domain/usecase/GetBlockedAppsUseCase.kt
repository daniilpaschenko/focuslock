package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository

/**
 * Returns the list of blocked applications
 */
class GetBlockedAppsUseCase(
    private val blockedAppsRepository: BlockedAppsRepository,
) {
    fun getBlockedApps(): List<App> = blockedAppsRepository.getBlockedApps()
}
