package com.rahmatsobrian.umkchecker.data.repository

import com.rahmatsobrian.umkchecker.data.local.AssetDataSeeder
import com.rahmatsobrian.umkchecker.data.local.dao.FavoriteDao
import com.rahmatsobrian.umkchecker.data.local.dao.SearchHistoryDao
import com.rahmatsobrian.umkchecker.data.local.dao.UmkDao
import com.rahmatsobrian.umkchecker.data.local.entity.FavoriteEntity
import com.rahmatsobrian.umkchecker.data.local.entity.SearchHistoryEntity
import com.rahmatsobrian.umkchecker.data.toDomain
import com.rahmatsobrian.umkchecker.di.IoDispatcher
import com.rahmatsobrian.umkchecker.domain.model.SearchHistoryItem
import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import com.rahmatsobrian.umkchecker.util.AppLogger
import com.rahmatsobrian.umkchecker.util.Constants
import com.rahmatsobrian.umkchecker.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UmkRepositoryImpl @Inject constructor(
    private val umkDao: UmkDao,
    private val favoriteDao: FavoriteDao,
    private val searchHistoryDao: SearchHistoryDao,
    private val assetDataSeeder: AssetDataSeeder,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : UmkRepository {

    override fun observeUmkList(
        query: String,
        province: String,
        regency: String,
        sortOrder: SortOrder
    ): Flow<List<Umk>> =
        umkDao.observeUmkList(
            query = query.trim(),
            province = province,
            regency = regency,
            sortOrder = sortOrder.name
        ).map { list -> list.map { it.toDomain() } }

    override fun observeUmkDetail(id: Long): Flow<Umk?> =
        umkDao.observeUmkById(id).map { it?.toDomain() }

    override fun observeFavorites(): Flow<List<Umk>> =
        umkDao.observeFavorites().map { list -> list.map { it.toDomain() } }

    override fun observeProvinces(): Flow<List<String>> = umkDao.observeProvinces()

    override fun observeRegencies(province: String): Flow<List<String>> =
        umkDao.observeRegencies(province)

    override suspend fun toggleFavorite(umk: Umk) = withContext(ioDispatcher) {
        if (umk.isFavorite) {
            favoriteDao.remove(umk.id)
        } else {
            favoriteDao.add(FavoriteEntity(umk.id))
        }
    }

    override fun observeSearchHistory(limit: Int): Flow<List<SearchHistoryItem>> =
        searchHistoryDao.observeRecent(limit).map { list ->
            list.map { SearchHistoryItem(it.id, it.query, it.timestamp) }
        }

    override suspend fun saveSearchQuery(query: String) = withContext(ioDispatcher) {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return@withContext
        searchHistoryDao.deleteByQuery(trimmed)
        searchHistoryDao.insert(SearchHistoryEntity(query = trimmed, timestamp = System.currentTimeMillis()))
        searchHistoryDao.trimTo(Constants.MAX_SEARCH_HISTORY)
    }

    override suspend fun deleteSearchQuery(query: String) = withContext(ioDispatcher) {
        searchHistoryDao.deleteByQuery(query)
    }

    override suspend fun clearSearchHistory() = withContext(ioDispatcher) {
        searchHistoryDao.clearAll()
    }

    override suspend fun refresh(): Resource<Unit> = withContext(ioDispatcher) {
        runCatching {
            umkDao.clearAll()
            assetDataSeeder.seedIfNeeded()
        }.fold(
            onSuccess = { Resource.Success(Unit) },
            onFailure = { throwable ->
                AppLogger.e(message = "Refresh failed", throwable = throwable)
                Resource.Error(message = "Gagal memuat data. Silakan coba lagi.", cause = throwable)
            }
        )
    }
}
