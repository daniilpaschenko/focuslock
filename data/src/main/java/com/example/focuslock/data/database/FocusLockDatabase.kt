package com.example.focuslock.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [BlockedAppEntity::class],
    version = 1,
    exportSchema = false, // in JSON-file
)
abstract class FocusLockDatabase : RoomDatabase() {

    abstract fun blockedAppsDao(): BlockedAppsDao
}
