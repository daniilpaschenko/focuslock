package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.BlockedApp

/**
 * Repository for managing blocked applications during focus sessions
 */
interface BlockedAppsRepository {
    fun getBlockedApps(): List<BlockedApp>

    fun addBlockedApp(app: BlockedApp)

    fun removeBlockedApp(packageName: String)
}
