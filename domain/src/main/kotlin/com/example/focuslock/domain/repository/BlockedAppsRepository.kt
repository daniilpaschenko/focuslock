package com.example.focuslock.domain.repository

import com.example.focuslock.domain.model.App

/**
 * Repository for managing blocked applications during focus sessions
 */
interface BlockedAppsRepository {
    fun getBlockedApps(): List<App>

    fun addBlockedApp(app: App)

    fun removeBlockedApp(packageName: String)
}
