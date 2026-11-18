package com.example.focuslock.feature.apps

import androidx.compose.runtime.Immutable
import com.example.focuslock.domain.model.App

sealed interface AppsUiState {

    data object Loading : AppsUiState

    /** empty list of installed apps */
    data object Empty : AppsUiState

    data object Error : AppsUiState

    // says that its properties and data must not change after creation
    @Immutable
    data class Content(
        val blockedApps: List<App>,
        /** already filtered by [searchQuery] */
        val allowedApps: List<App>,
        val searchQuery: String = "",
    ) : AppsUiState {
        val isSearching: Boolean get() = searchQuery.isNotBlank()
    }
}