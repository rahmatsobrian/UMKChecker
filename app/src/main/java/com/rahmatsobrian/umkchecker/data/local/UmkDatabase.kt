package com.rahmatsobrian.umkchecker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rahmatsobrian.umkchecker.data.local.dao.FavoriteDao
import com.rahmatsobrian.umkchecker.data.local.dao.SearchHistoryDao
import com.rahmatsobrian.umkchecker.data.local.dao.UmkDao
import com.rahmatsobrian.umkchecker.data.local.entity.FavoriteEntity
import com.rahmatsobrian.umkchecker.data.local.entity.SearchHistoryEntity
import com.rahmatsobrian.umkchecker.data.local.entity.UmkEntity

@Database(
    entities = [UmkEntity::class, FavoriteEntity::class, SearchHistoryEntity::class],
    version = 1,
    exportSchema = true
)
abstract class UmkDatabase : RoomDatabase() {
    abstract fun umkDao(): UmkDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}
