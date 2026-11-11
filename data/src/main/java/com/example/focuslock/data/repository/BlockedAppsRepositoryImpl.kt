package com.example.focuslock.data.repository

import com.example.focuslock.data.database.BlockedAppEntity
import com.example.focuslock.data.database.BlockedAppsDao
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stores and retrieves the apps blocked during focus sessions
 */
@Singleton
class BlockedAppsRepositoryImpl @Inject constructor(
    private val blockedAppsDao: BlockedAppsDao,
) : BlockedAppsRepository {

    override fun getBlockedApps(): List<App> =
        blockedAppsDao.getBlockedApps().map { blockedApp ->
            App(
                packageName = blockedApp.packageName,
                appName = blockedApp.appName,
            )
        }

    override fun addBlockedApp(app: App) {
        blockedAppsDao.addBlockedApp(
            BlockedAppEntity(
                packageName = app.packageName,
                appName = app.appName,
            ),
        )
    }

    override fun removeBlockedApp(packageName: String) {
        blockedAppsDao.removeBlockedApp(packageName)
    }
}
