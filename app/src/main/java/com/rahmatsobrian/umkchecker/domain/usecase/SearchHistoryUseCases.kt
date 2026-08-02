package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.model.SearchHistoryItem
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import com.rahmatsobrian.umkchecker.util.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchHistoryUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    operator fun invoke(limit: Int = Constants.MAX_SEARCH_HISTORY): Flow<List<SearchHistoryItem>> =
        repository.observeSearchHistory(limit)
}

class SaveSearchHistoryUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    suspend operator fun invoke(query: String) = repository.saveSearchQuery(query)
}

class DeleteSearchHistoryItemUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    suspend operator fun invoke(query: String) = repository.deleteSearchQuery(query)
}

class ClearSearchHistoryUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    suspend operator fun invoke() = repository.clearSearchHistory()
}
