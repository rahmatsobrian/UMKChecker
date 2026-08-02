package com.rahmatsobrian.umkchecker.domain.repository

import com.rahmatsobrian.umkchecker.domain.model.SearchHistoryItem
import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.util.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Abstraction the whole app (ViewModels/UseCases) depends on. The implementation
 * currently reads from Room only, but because everything is expressed through
 * this interface, swapping in (or adding) a remote data source later only means
 * writing a new implementation — no caller needs to change.
 */
interface UmkRepository {

    fun observeUmkList(
        query: String,
        province: String,
        regency: String,
        sortOrder: SortOrder
    ): Flow<List<Umk>>

    fun observeUmkDetail(id: Long): Flow<Umk?>

    fun observeFavorites(): Flow<List<Umk>>

    fun observeProvinces(): Flow<List<String>>

    fun observeRegencies(province: String): Flow<List<String>>

    suspend fun toggleFavorite(umk: Umk)

    fun observeSearchHistory(limit: Int): Flow<List<SearchHistoryItem>>

    suspend fun saveSearchQuery(query: String)

    suspend fun deleteSearchQuery(query: String)

    suspend fun clearSearchHistory()

    /** Re-runs the seeding/refresh routine; used by pull-to-refresh and retry actions. */
    suspend fun refresh(): Resource<Unit>
}
