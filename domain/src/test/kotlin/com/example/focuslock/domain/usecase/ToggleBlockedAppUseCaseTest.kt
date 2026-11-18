package com.example.focuslock.domain.usecase

import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ToggleBlockedAppUseCaseTest {

    private val app = App(packageName = "com.example.app", appName = "Example")

    @Test
    fun `toggleBlockedApp adds app and returns true when app is not blocked`() = runBlocking {
        val repository = FakeBlockedAppsRepository()
        val useCase = ToggleBlockedAppUseCase(repository)

        val result = useCase.toggleBlockedApp(app)

        assertTrue(result)
        assertTrue(repository.isAppBlocked(app.packageName))
        assertEquals(listOf(app), repository.currentBlockedApps())
    }

    @Test
    fun `toggleBlockedApp removes app and returns false when app is already blocked`() = runBlocking {
        val repository = FakeBlockedAppsRepository(initialBlockedApps = listOf(app))
        val useCase = ToggleBlockedAppUseCase(repository)

        val result = useCase.toggleBlockedApp(app)

        assertFalse(result)
        assertFalse(repository.isAppBlocked(app.packageName))
        assertTrue(repository.currentBlockedApps().isEmpty())
    }

    private class FakeBlockedAppsRepository(
        initialBlockedApps: List<App> = emptyList(),
    ) : BlockedAppsRepository {

        private val blockedApps = MutableStateFlow(initialBlockedApps)

        override fun observeBlockedApps(): Flow<List<App>> = blockedApps

        override suspend fun addBlockedApp(app: App) {
            if (blockedApps.value.none { it.packageName == app.packageName }) {
                blockedApps.value += app
            }
        }

        override suspend fun removeBlockedApp(packageName: String) {
            blockedApps.value = blockedApps.value.filterNot { it.packageName == packageName }
        }

        override suspend fun isAppBlocked(packageName: String): Boolean =
            blockedApps.value.any { it.packageName == packageName }

        fun currentBlockedApps(): List<App> = blockedApps.value
    }
}
