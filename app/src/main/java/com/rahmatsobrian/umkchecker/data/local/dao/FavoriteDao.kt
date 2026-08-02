package com.rahmatsobrian.umkchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahmatsobrian.umkchecker.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun add(favorite: FavoriteEntity)

    @Query("DELETE FROM favorite_table WHERE umkId = :umkId")
    suspend fun remove(umkId: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_table WHERE umkId = :umkId)")
    suspend fun isFavorite(umkId: Long): Boolean
}
