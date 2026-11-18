package com.example.focuslock.feature.apps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.focuslock.R
import com.example.focuslock.domain.model.App
import com.example.focuslock.ui.theme.FocuslockTheme

/** connects ViewModel and stateless-screen */
@Composable
fun AppsRoute(
    viewModel: AppsViewModel,
    modifier: Modifier = Modifier,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AppsScreen(
        state = state,
        onQueryChanged = viewModel::onQueryChanged,
        onAppSelectionChanged = viewModel::onAppSelectionChanged,
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(
    state: AppsUiState,
    onQueryChanged: (String) -> Unit,
    onAppSelectionChanged: (App, Boolean) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.apps_title)) })
        },
    ) { innerPadding ->
        val contentModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (state) {
            AppsUiState.Loading -> Box(contentModifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }

            AppsUiState.Empty -> CenteredMessage(
                text = stringResource(R.string.apps_empty),
                modifier = contentModifier,
            )

            AppsUiState.Error -> CenteredMessage(
                text = stringResource(R.string.apps_error),
                modifier = contentModifier,
            ) {
                Button(onClick = onRetry) { Text(stringResource(R.string.apps_retry)) }
            }

            is AppsUiState.Content -> AppsContent(
                state = state,
                onQueryChanged = onQueryChanged,
                onAppSelectionChanged = onAppSelectionChanged,
                modifier = contentModifier.padding(horizontal = 16.dp),
            )
        }
    }
}

@Composable
private fun AppsContent(
    state: AppsUiState.Content,
    onQueryChanged: (String) -> Unit,
    onAppSelectionChanged: (App, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {

        OutlinedTextField(
            value = state.searchQuery,
            onValueChange = onQueryChanged,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            placeholder = { Text(stringResource(R.string.apps_search_placeholder)) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = stringResource(R.string.apps_search_icon_description),
                )
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            // --- Blocked ---
            item(key = "blocked_header") {
                Text(
                    text = stringResource(R.string.apps_blocked_header, state.blockedApps.size),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 12.dp),
                )
            }

            if (state.blockedApps.isEmpty()) {
                item(key = "blocked_hint") {
                    Text(
                        text = stringResource(R.string.apps_blocked_hint),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 12.dp),
                    )
                }
            } else {
                items(items = state.blockedApps, key = { "blocked_${it.packageName}" }) { app ->
                    AppRow(
                        app = app,
                        isSelected = true,
                        onSelectionChanged = { onAppSelectionChanged(app, false) },
                    )
                }
            }

            // --- Allowed ---
            item(key = "allowed_header") {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                    Text(
                        text = stringResource(R.string.apps_allowed_header),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp),
                    )
                }
            }

            if (state.allowedApps.isEmpty()) {
                item(key = "allowed_empty") {
                    Text(
                        text = stringResource(
                            if (state.isSearching) R.string.apps_no_results
                            else R.string.apps_all_blocked
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            } else {
                items(items = state.allowedApps, key = { "allowed_${it.packageName}" }) { app ->
                    AppRow(
                        app = app,
                        isSelected = false,
                        onSelectionChanged = { selected -> onAppSelectionChanged(app, selected) },
                    )
                }
            }
        }
    }
}

@Composable
private fun CenteredMessage(
    text: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        if (action != null) {
            Box(Modifier.padding(top = 16.dp)) { action() }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppsScreenPreview() {
    val youtube = App(packageName = "com.youtube", appName = "YouTube")
    val tiktok = App(packageName = "com.tiktok", appName = "TikTok")
    val others = listOf(
        App(packageName = "com.instagram", appName = "Instagram"),
        App(packageName = "com.telegram", appName = "Telegram"),
        App(packageName = "com.chrome", appName = "Chrome"),
    )

    var blocked by remember { mutableStateOf(listOf(youtube, tiktok)) }
    var allowed by remember { mutableStateOf(others) }
    var query by remember { mutableStateOf("") }

    FocuslockTheme {
        AppsScreen(
            state = AppsUiState.Content(
                blockedApps = blocked,
                allowedApps = allowed.filter { it.appName.contains(query, ignoreCase = true) },
                searchQuery = query,
            ),
            onQueryChanged = { query = it },
            onAppSelectionChanged = { app, isBlocked ->
                if (isBlocked) {
                    allowed = allowed - app
                    blocked = blocked + app
                } else {
                    blocked = blocked - app
                    allowed = allowed + app
                }
            },
            onRetry = {},
        )
    }
}