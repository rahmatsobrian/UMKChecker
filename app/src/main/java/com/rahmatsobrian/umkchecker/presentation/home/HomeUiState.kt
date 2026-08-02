package com.rahmatsobrian.umkchecker.presentation.home

import com.rahmatsobrian.umkchecker.domain.model.SearchHistoryItem
import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.domain.model.Umk

data class HomeUiState(
    val isLoading: Boolean = true,
    val isRefreshing: Boolean = false,
    val umkList: List<Umk> = emptyList(),
    val provinces: List<String> = emptyList(),
    val regencies: List<String> = emptyList(),
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchHistory: List<SearchHistoryItem> = emptyList(),
    val selectedProvince: String = "",
    val selectedRegency: String = "",
    val sortOrder: SortOrder = SortOrder.NAME_ASC,
    val errorMessage: String? = null,
    val snackbarMessage: String? = null
) {
    val isEmpty: Boolean
        get() = !isLoading && errorMessage == null && umkList.isEmpty()

    val hasActiveFilter: Boolean
        get() = selectedProvince.isNotEmpty() || selectedRegency.isNotEmpty() || sortOrder != SortOrder.NAME_ASC
}
