package com.example.focuslock.feature.apps

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import com.example.focuslock.domain.repository.InstalledAppsRepository
import com.example.focuslock.domain.usecase.GetInstalledAppsUseCase
import com.example.focuslock.domain.usecase.ObserveBlockedAppsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class AppsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val installedAppsRepository: InstalledAppsRepository = mock()
    private val blockedAppsRepository: BlockedAppsRepository = mock()

    private val youtube = App(packageName = "com.google.android.youtube", appName = "YouTube")
    private val chrome = App(packageName = "com.android.chrome", appName = "Chrome")
    private val telegram = App(packageName = "org.telegram.messenger", appName = "Telegram")

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `uiState emits Loading then Content with blocked and allowed apps`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps()).thenReturn(listOf(chrome, youtube, telegram))
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(listOf(youtube)))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            // run pending coroutines until data loads
            advanceUntilIdle()

            val content = awaitItem()
            assertTrue(content is AppsUiState.Content)
            content as AppsUiState.Content
            assertEquals(listOf(youtube), content.blockedApps)
            assertEquals(listOf(chrome, telegram), content.allowedApps)
            assertEquals("", content.searchQuery)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Empty when there are no installed apps`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps()).thenReturn(emptyList())
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()

            assertEquals(AppsUiState.Empty, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `uiState emits Error when loading installed apps fails`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps()).thenThrow(RuntimeException("boom"))
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()

            assertEquals(AppsUiState.Error, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onQueryChanged filters allowed apps by search query`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps()).thenReturn(listOf(chrome, youtube, telegram))
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // initial content

            viewModel.onQueryChanged("tel")
            advanceUntilIdle()

            val filtered = awaitItem() as AppsUiState.Content
            assertEquals("tel", filtered.searchQuery)
            assertEquals(listOf(telegram), filtered.allowedApps)
            assertTrue(filtered.blockedApps.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onAppSelectionChanged adds or removes blocked app`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps()).thenReturn(listOf(chrome, youtube))
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()
            awaitItem() // initial content

            viewModel.onAppSelectionChanged(youtube, blocked = true)
            advanceUntilIdle()
            verify(blockedAppsRepository).addBlockedApp(youtube)

            viewModel.onAppSelectionChanged(youtube, blocked = false)
            advanceUntilIdle()
            verify(blockedAppsRepository).removeBlockedApp(youtube.packageName)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `onRetry reloads installed apps after error`() = runTest(testDispatcher) {
        whenever(installedAppsRepository.getInstalledApps())
            .thenThrow(RuntimeException("boom"))
            .thenReturn(listOf(chrome))
        whenever(blockedAppsRepository.observeBlockedApps()).thenReturn(flowOf(emptyList()))

        val viewModel = createViewModel()

        viewModel.uiState.test {
            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()
            assertEquals(AppsUiState.Error, awaitItem())

            viewModel.onRetry()
            advanceUntilIdle()

            assertEquals(AppsUiState.Loading, awaitItem())
            advanceUntilIdle()
            val content = awaitItem() as AppsUiState.Content
            assertEquals(listOf(chrome), content.allowedApps)

            cancelAndIgnoreRemainingEvents()
        }
    }

    private fun createViewModel(
        savedStateHandle: SavedStateHandle = SavedStateHandle(),
    ): AppsViewModel = AppsViewModel(
        savedStateHandle = savedStateHandle,
        getInstalledApps = GetInstalledAppsUseCase(installedAppsRepository),
        observeBlockedApps = ObserveBlockedAppsUseCase(blockedAppsRepository),
        blockedAppsRepository = blockedAppsRepository,
    )
}
