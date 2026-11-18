package com.example.focuslock.data.repository

import com.example.focuslock.data.database.BlockedAppEntity
import com.example.focuslock.data.database.BlockedAppsDao
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Stores and retrieves the apps blocked during focus sessions
 */
@Singleton
class BlockedAppsRepositoryImpl @Inject constructor(
    private val blockedAppsDao: BlockedAppsDao,
) : BlockedAppsRepository {

    override fun observeBlockedApps(): Flow<List<App>> =
        blockedAppsDao.observeBlockedApps().map { entities ->
            entities.map { App(packageName = it.packageName, appName = it.appName) }
        }

    override suspend fun addBlockedApp(app: App) {
        blockedAppsDao.addBlockedApp(
            BlockedAppEntity(packageName = app.packageName, appName = app.appName)
        )
    }

    override suspend fun removeBlockedApp(packageName: String) {
        blockedAppsDao.removeBlockedApp(packageName)
    }

    override suspend fun isAppBlocked(packageName: String): Boolean =
        blockedAppsDao.isBlocked(packageName)
}
