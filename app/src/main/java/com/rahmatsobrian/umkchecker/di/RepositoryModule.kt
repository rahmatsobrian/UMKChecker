package com.rahmatsobrian.umkchecker.di

import com.rahmatsobrian.umkchecker.data.repository.UmkRepositoryImpl
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUmkRepository(impl: UmkRepositoryImpl): UmkRepository
}
