package com.rahmatsobrian.umkchecker.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rahmatsobrian.umkchecker.data.local.entity.UmkEntity
import com.rahmatsobrian.umkchecker.data.local.entity.UmkWithFavorite
import kotlinx.coroutines.flow.Flow

@Dao
interface UmkDao {

    /**
     * Single reactive query that powers the whole home list: search, province/regency
     * filters and sorting are all pushed down to SQLite so the list stays fast even
     * with thousands of rows. Empty filter params ('%%') are treated as "no filter".
     */
    @Query(
        """
        SELECT umk_table.*,
               CASE WHEN favorite_table.umkId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
        FROM umk_table
        LEFT JOIN favorite_table ON umk_table.id = favorite_table.umkId
        WHERE (:query = '' OR umk_table.regionName LIKE '%' || :query || '%' OR umk_table.provinceName LIKE '%' || :query || '%')
        AND (:province = '' OR umk_table.provinceName = :province)
        AND (:regency = '' OR umk_table.regionName = :regency)
        ORDER BY
            CASE WHEN :sortOrder = 'NAME_ASC' THEN umk_table.regionName END ASC,
            CASE WHEN :sortOrder = 'NAME_DESC' THEN umk_table.regionName END DESC,
            CASE WHEN :sortOrder = 'AMOUNT_DESC' THEN umk_table.amount END DESC,
            CASE WHEN :sortOrder = 'AMOUNT_ASC' THEN umk_table.amount END ASC,
            umk_table.regionName ASC
        """
    )
    fun observeUmkList(
        query: String,
        province: String,
        regency: String,
        sortOrder: String
    ): Flow<List<UmkWithFavorite>>

    @Query(
        """
        SELECT umk_table.*,
               CASE WHEN favorite_table.umkId IS NOT NULL THEN 1 ELSE 0 END AS isFavorite
        FROM umk_table
        LEFT JOIN favorite_table ON umk_table.id = favorite_table.umkId
        WHERE umk_table.id = :id
        LIMIT 1
        """
    )
    fun observeUmkById(id: Long): Flow<UmkWithFavorite?>

    @Query(
        """
        SELECT umk_table.*,
               1 AS isFavorite
        FROM umk_table
        INNER JOIN favorite_table ON umk_table.id = favorite_table.umkId
        ORDER BY umk_table.regionName ASC
        """
    )
    fun observeFavorites(): Flow<List<UmkWithFavorite>>

    @Query("SELECT DISTINCT provinceName FROM umk_table ORDER BY provinceName ASC")
    fun observeProvinces(): Flow<List<String>>

    @Query("SELECT DISTINCT regionName FROM umk_table WHERE (:province = '' OR provinceName = :province) ORDER BY regionName ASC")
    fun observeRegencies(province: String): Flow<List<String>>

    @Query("SELECT COUNT(*) FROM umk_table")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<UmkEntity>)

    @Query("DELETE FROM umk_table")
    suspend fun clearAll()
}
