package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.InstalledAppsRepository
import javax.inject.Inject

/**
 * Returns the list of installed applications
 */
class GetInstalledAppsUseCase @Inject constructor(
    private val installedAppsRepository: InstalledAppsRepository,
) {
    // overloads operator ()
    suspend operator fun invoke(): List<App> = installedAppsRepository.getInstalledApps()
}
