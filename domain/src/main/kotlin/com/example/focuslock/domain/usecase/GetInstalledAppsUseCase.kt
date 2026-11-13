package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.InstalledAppsRepository

/**
 * Returns the list of installed applications
 */
class GetInstalledAppsUseCase(
    private val installedAppsRepository: InstalledAppsRepository,
) {
    suspend fun getInstalledApps(): List<App> = installedAppsRepository.getInstalledApps()
}
