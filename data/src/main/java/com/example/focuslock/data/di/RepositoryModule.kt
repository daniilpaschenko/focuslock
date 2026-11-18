package com.example.focuslock.data.di

import com.example.focuslock.data.repository.BlockedAppsRepositoryImpl
import com.example.focuslock.data.repository.InstalledAppsRepositoryImpl
import com.example.focuslock.domain.repository.BlockedAppsRepository
import com.example.focuslock.domain.repository.InstalledAppsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // "this class contains instructions for creating and providing dependencies"
@InstallIn(SingletonComponent::class) // "dependencies from this module are available at the app lvl"
abstract class RepositoryModule {

    /**
     * "When someone requests an InstalledAppsRepository,
     * Hilt should provide them with an InstalledAppsRepositoryImpl"
     */
    @Binds
    @Singleton
    abstract fun bindInstalledAppsRepository(
        impl: InstalledAppsRepositoryImpl,
    ): InstalledAppsRepository

    @Binds
    @Singleton
    abstract fun bindBlockedAppsRepository(
        impl: BlockedAppsRepositoryImpl,
    ): BlockedAppsRepository
}