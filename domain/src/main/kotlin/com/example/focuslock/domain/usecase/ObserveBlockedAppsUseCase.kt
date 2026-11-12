package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import kotlinx.coroutines.flow.Flow

/**
 * Returns the list of blocked applications
 */
class ObserveBlockedAppsUseCase(
    private val blockedAppsRepository: BlockedAppsRepository,
) {
    fun observeBlockedApps(): Flow<List<App>> = blockedAppsRepository.observeBlockedApps()
}
