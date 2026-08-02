package com.rahmatsobrian.umkchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahmatsobrian.umkchecker.data.local.entity.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchHistoryDao {

    @Query("SELECT * FROM search_history_table ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<SearchHistoryEntity>>

    @Query("DELETE FROM search_history_table WHERE query = :query")
    suspend fun deleteByQuery(query: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchHistoryEntity)

    @Query("DELETE FROM search_history_table")
    suspend fun clearAll()

    @Query(
        """
        DELETE FROM search_history_table
        WHERE id NOT IN (SELECT id FROM search_history_table ORDER BY timestamp DESC LIMIT :keep)
        """
    )
    suspend fun trimTo(keep: Int)
}
