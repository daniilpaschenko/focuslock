package com.example.focuslock.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.InstalledAppsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides the list of installed applications through the system PackageManager.
 *
 * Only applications that expose a launcher intent (Intent with the
 * `CATEGORY_LAUNCHER` category) are returned, since only such apps are
 * meaningful to be blocked during a focus session
 */
@Singleton
class InstalledAppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : InstalledAppsRepository {

    override fun getInstalledApps(): List<App> {
        val packageManager = context.packageManager
        // creates Intent with CATEGORY_LAUNCHER
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        // searches for Activities that can handle the passed Intent
        return packageManager.queryIntentActivities(launcherIntent, 0)
            .mapNotNull { resolveInfo ->
                // if null, skips only this element
                val activityInfo = resolveInfo.activityInfo ?: return@mapNotNull null
                // gets packageName
                val packageName = activityInfo.packageName
                // gets the user-facing name of an app
                val appName = packageManager.getApplicationLabel(
                    packageManager.getApplicationInfo(
                        packageName,
                        // flag that requests app metadata
                        PackageManager.GET_META_DATA
                    ),
                ).toString()

                App(
                    packageName = packageName,
                    appName = appName,
                )
            }
            // removes duplicates by packageName
            .distinctBy { it.packageName }
    }
}
