package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.App

/**
 * Repository for retrieving installed applications on the device
 */

interface InstalledAppsRepository {

    suspend fun getInstalledApps(): List<App>
}