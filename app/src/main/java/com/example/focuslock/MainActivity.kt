package com.example.focuslock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.focuslock.feature.apps.AppsRoute
import com.example.focuslock.feature.apps.AppsViewModel
import com.example.focuslock.ui.theme.FocuslockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FocuslockTheme {
                val viewModel: AppsViewModel = hiltViewModel()
                AppsRoute(viewModel = viewModel)
            }
        }
    }
}