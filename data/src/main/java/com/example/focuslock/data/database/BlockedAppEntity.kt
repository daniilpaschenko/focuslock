package com.example.focuslock.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room representation of an app blocked during a focus session
 */
@Entity(tableName = "blocked_apps")
data class BlockedAppEntity(
    @PrimaryKey
    val packageName: String, // unique identifier
    val appName: String, // display name
)
