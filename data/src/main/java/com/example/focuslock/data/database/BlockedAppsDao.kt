package com.example.focuslock.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Access to the `blocked_apps` table
 */
@Dao
interface BlockedAppsDao {

    @Query("SELECT * FROM blocked_apps")
    fun getBlockedApps(): List<BlockedAppEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addBlockedApp(blockedApp: BlockedAppEntity): Int // returns rowId

    @Query("DELETE FROM blocked_apps WHERE packageName = :packageName")
    fun removeBlockedApp(packageName: String): Int // returns rowId
}
