package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Returns the list of blocked applications
 */
class ObserveBlockedAppsUseCase @Inject constructor(
    private val blockedAppsRepository: BlockedAppsRepository,
) {
    // overloads operator ()
    operator fun invoke(): Flow<List<App>> = blockedAppsRepository.observeBlockedApps()
}
