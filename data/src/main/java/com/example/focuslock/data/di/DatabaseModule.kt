package com.example.focuslock.data.di

import android.content.Context
import androidx.room.Room
import com.example.focuslock.data.database.BlockedAppsDao
import com.example.focuslock.data.database.FocusLockDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideFocusLockDatabase(
        @ApplicationContext context: Context,
    ): FocusLockDatabase =
        Room.databaseBuilder(
            context,
            FocusLockDatabase::class.java,
            "focuslock.db",
        ).build()

    @Provides
    fun provideBlockedAppsDao(
        database: FocusLockDatabase,
    ): BlockedAppsDao = database.blockedAppsDao()
}
