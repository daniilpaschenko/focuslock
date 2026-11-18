package com.example.focuslock.data.repository

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.os.Build
import com.example.focuslock.domain.model.App
import com.example.focuslock.domain.repository.InstalledAppsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Provides the list of installed applications through the system PackageManager
 *
 * Only applications that expose a launcher activity (`CATEGORY_LAUNCHER`) are
 * returned, since only such apps are meaningful to block during a focus session.
 * The FocusLock app itself is excluded
 */
@Singleton
class InstalledAppsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : InstalledAppsRepository {

    // execute code in another coroutine dispatcher (IO = input output)
    override suspend fun getInstalledApps(): List<App> = withContext(Dispatchers.IO) {
        val packageManager = context.packageManager
        val launcherIntent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        packageManager.queryLauncherActivities(launcherIntent)
            .asSequence()
            .filter { it.activityInfo != null }
            // dedupe first, so labels are not loaded for duplicates
            .distinctBy { it.activityInfo.packageName }
            .filter { it.activityInfo.packageName != context.packageName }
            .map { resolveInfo ->
                App(
                    packageName = resolveInfo.activityInfo.packageName,
                    // no extra getApplicationInfo() call, so no NameNotFoundException
                    appName = resolveInfo.loadLabel(packageManager).toString(),
                )
            }
            .toList()
    }

    /**
     * Queries launcher activities using the appropriate API based on the Android version
     */
    private fun PackageManager.queryLauncherActivities(intent: Intent): List<ResolveInfo> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            queryIntentActivities(intent, PackageManager.ResolveInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            queryIntentActivities(intent, 0)
        }
}