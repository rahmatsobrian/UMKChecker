package com.rahmatsobrian.umkchecker.di

import android.content.Context
import androidx.room.Room
import com.rahmatsobrian.umkchecker.data.local.UmkDatabase
import com.rahmatsobrian.umkchecker.data.local.dao.FavoriteDao
import com.rahmatsobrian.umkchecker.data.local.dao.SearchHistoryDao
import com.rahmatsobrian.umkchecker.data.local.dao.UmkDao
import com.rahmatsobrian.umkchecker.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UmkDatabase =
        Room.databaseBuilder(context, UmkDatabase::class.java, Constants.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideUmkDao(database: UmkDatabase): UmkDao = database.umkDao()

    @Provides
    fun provideFavoriteDao(database: UmkDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun provideSearchHistoryDao(database: UmkDatabase): SearchHistoryDao = database.searchHistoryDao()

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }
}
