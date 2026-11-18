package com.example.focuslock.feature.apps

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.BlockedAppsRepository
import com.example.focuslock.domain.usecase.GetInstalledAppsUseCase
import com.example.focuslock.domain.usecase.ObserveBlockedAppsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle, // stored search query
    private val getInstalledApps: GetInstalledAppsUseCase,
    observeBlockedApps: ObserveBlockedAppsUseCase,
    private val blockedAppsRepository: BlockedAppsRepository,
) : ViewModel() {

    // null = still loading
    private val installedApps = MutableStateFlow<Result<List<App>>?>(null)

    private val searchQuery: StateFlow<String> =
        savedStateHandle.getStateFlow(KEY_QUERY, "")

    private val blockedApps = observeBlockedApps()
        .map { apps -> apps.mapTo(HashSet()) { it.packageName } }

    // combines several flows
    val uiState: StateFlow<AppsUiState> = combine(
        installedApps,
        blockedApps,
        searchQuery,
    ) { installedResult, blocked, query ->
        when {
            installedResult == null -> AppsUiState.Loading
            installedResult.isFailure -> AppsUiState.Error
            else -> {
                val installed = installedResult.getOrThrow()
                if (installed.isEmpty()) {
                    AppsUiState.Empty
                } else {
                    // splits the list into two parts
                    val (blockedApps, others) = installed.partition { it.packageName in blocked }
                    AppsUiState.Content(
                        blockedApps = blockedApps.sortedBy { it.appName.lowercase() },
                        allowedApps = others
                            .filter { it.appName.contains(query.trim(), ignoreCase = true) }
                            .sortedBy { it.appName.lowercase() },
                        searchQuery = query,
                    )
                }
            }
        }
    // converting Flow to StateFlow
    }.stateIn(
        // specifies the coroutine scope in which the collection will be performed
        scope = viewModelScope,
        // flow will begin collecting data only when the first active subscriber appears
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AppsUiState.Loading,
    )

    // is executed when a ViewModel object is created
    init {
        loadInstalledApps()
    }

    fun onQueryChanged(query: String) {
        savedStateHandle[KEY_QUERY] = query
    }

    fun onAppSelectionChanged(app: App, blocked: Boolean) {
        viewModelScope.launch {
            if (blocked) {
                blockedAppsRepository.addBlockedApp(app)
            } else {
                blockedAppsRepository.removeBlockedApp(app.packageName)
            }
        }
    }

    fun onRetry() {
        installedApps.value = null
        loadInstalledApps()
    }

    private fun loadInstalledApps() {
        // does not block the main thread
        viewModelScope.launch {
            installedApps.value = runCatching { getInstalledApps() }
        }
    }

    private companion object {
        const val KEY_QUERY = "query"
    }
}