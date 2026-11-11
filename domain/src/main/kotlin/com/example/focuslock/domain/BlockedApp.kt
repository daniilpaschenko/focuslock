package com.example.focuslock.domain

/**
 * Describes an application that should be blocked during a focus session
 */
data class BlockedApp(
    val packageName: String,
    val appName: String,
)
