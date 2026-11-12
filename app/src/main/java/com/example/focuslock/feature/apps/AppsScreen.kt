package com.example.focuslock.feature.apps

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.focuslock.domain.model.App

import androidx.compose.ui.tooling.preview.Preview
import com.example.focuslock.ui.theme.FocuslockTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppsScreen(
    installedApps: List<App>,
    blockedPackageNames: Set<String>,
    onAppSelectionChanged: (App, Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember {
        mutableStateOf("")
    }

    val selectedApps = remember(installedApps, blockedPackageNames) {
        installedApps.filter {
            it.packageName in blockedPackageNames
        }
    }

    val filteredApps = remember(
        installedApps,
        blockedPackageNames,
        searchQuery
    ) {
        installedApps.filter { app ->
            app.packageName !in blockedPackageNames &&
                    app.appName.contains(
                        searchQuery,
                        ignoreCase = true
                    )
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text("All apps")
                }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            // Search
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                placeholder = {
                    Text("Search for apps")
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(14.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {

                // Selected apps section
                item {
                    Text(
                        text = "Blocked (${selectedApps.size})",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            vertical = 12.dp
                        )
                    )
                }

                if (selectedApps.isEmpty()) {
                    item {
                        Text(
                            text = "Select the apps you want to block from the list below",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(
                                vertical = 12.dp
                            )
                        )
                    }
                } else {
                    items(
                        items = selectedApps,
                        key = { it.packageName }
                    ) { app ->
                        AppRow(
                            app = app,
                            isSelected = true,
                            onSelectionChanged = {
                                onAppSelectionChanged(app, false)
                            }
                        )
                    }
                }

                item {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Text(
                        text = "Unblocked",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(
                            vertical = 8.dp
                        )
                    )
                }

                // Installed apps section
                if (filteredApps.isEmpty()) {
                    item {
                        Text(
                            text = "No applications found",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                    }
                } else {
                    items(
                        items = filteredApps,
                        key = { it.packageName }
                    ) { app ->
                        AppRow(
                            app = app,
                            isSelected = false,
                            onSelectionChanged = { selected ->
                                onAppSelectionChanged(app, selected)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AppsScreenPreview() {
    // test list of apps
    val sampleApps = listOf(
        App(packageName = "com.youtube", appName = "YouTube"),
        App(packageName = "com.instagram", appName = "Instagram"),
        App(packageName = "com.telegram", appName = "Telegram"),
        App(packageName = "com.tiktok", appName = "TikTok"),
        App(packageName = "com.chrome", appName = "Chrome")
    )

    var blockedPackages by remember {
        mutableStateOf(setOf("com.youtube", "com.tiktok"))
    }

    FocuslockTheme {
        AppsScreen(
            installedApps = sampleApps,
            blockedPackageNames = blockedPackages,
            onAppSelectionChanged = { app, isSelected ->
                blockedPackages = if (isSelected) {
                    blockedPackages + app.packageName
                } else {
                    blockedPackages - app.packageName
                }
            }
        )
    }
}