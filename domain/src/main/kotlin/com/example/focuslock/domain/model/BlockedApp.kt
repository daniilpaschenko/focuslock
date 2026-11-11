package com.example.focuslock.domain.model

/**
 * Describes an application that should be blocked during a focus session.
 */
data class BlockedApp(
    val packageName: String,
    val appName: String,
)
