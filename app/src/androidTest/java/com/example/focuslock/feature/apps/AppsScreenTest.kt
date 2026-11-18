package com.example.focuslock.feature.apps

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.focuslock.domain.model.App
import com.example.focuslock.ui.theme.FocuslockTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appsScreen_showsSearchBlockedAndAllowedSections_andTogglesSelection() {
        val youtube = App(packageName = "com.google.android.youtube", appName = "YouTube")
        val chrome = App(packageName = "com.android.chrome", appName = "Chrome")
        var toggledApp: App? = null
        var toggledBlocked: Boolean? = null

        composeTestRule.setContent {
            FocuslockTheme {
                AppsScreen(
                    state = AppsUiState.Content(
                        blockedApps = listOf(youtube),
                        allowedApps = listOf(chrome),
                        searchQuery = "",
                    ),
                    onQueryChanged = {},
                    onAppSelectionChanged = { app, isBlocked ->
                        toggledApp = app
                        toggledBlocked = isBlocked
                    },
                    onRetry = {},
                )
            }
        }

        composeTestRule.onNodeWithText("Choose apps").assertIsDisplayed()
        composeTestRule.onNodeWithText("Search for apps").assertIsDisplayed()
        composeTestRule.onNodeWithText("Blocked (1)").assertIsDisplayed()
        composeTestRule.onNodeWithText("Allowed").assertIsDisplayed()
        composeTestRule.onNodeWithText("YouTube").assertIsDisplayed()
        composeTestRule.onNodeWithText("Chrome").assertIsDisplayed()

        // Tapping a blocked app row triggers unblocking callback in this screen state.
        composeTestRule.onNodeWithText("YouTube").performClick()

        assertEquals(youtube, toggledApp)
        assertEquals(false, toggledBlocked)
    }
}
