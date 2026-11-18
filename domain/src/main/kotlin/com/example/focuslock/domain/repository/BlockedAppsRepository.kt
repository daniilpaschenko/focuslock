package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.App
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing blocked applications during focus sessions
 */
interface BlockedAppsRepository {
    fun observeBlockedApps(): Flow<List<App>>
    suspend fun addBlockedApp(app: App)
    suspend fun removeBlockedApp(packageName: String)

    suspend fun isAppBlocked(packageName: String): Boolean
}
