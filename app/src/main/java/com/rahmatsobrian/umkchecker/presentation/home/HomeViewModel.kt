package com.rahmatsobrian.umkchecker.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.usecase.ClearSearchHistoryUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.DeleteSearchHistoryItemUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.GetProvincesUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.GetRegenciesUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.GetSearchHistoryUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.GetUmkListUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.RefreshUmkDataUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.SaveSearchHistoryUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.ToggleFavoriteUseCase
import com.rahmatsobrian.umkchecker.util.Constants
import com.rahmatsobrian.umkchecker.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUmkListUseCase: GetUmkListUseCase,
    private val getProvincesUseCase: GetProvincesUseCase,
    private val getRegenciesUseCase: GetRegenciesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val saveSearchHistoryUseCase: SaveSearchHistoryUseCase,
    private val deleteSearchHistoryItemUseCase: DeleteSearchHistoryItemUseCase,
    private val clearSearchHistoryUseCase: ClearSearchHistoryUseCase,
    private val refreshUmkDataUseCase: RefreshUmkDataUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    init {
        observeUmkList()
        observeFilterOptions()
        observeSearchHistory()
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = refreshUmkDataUseCase()) {
                is Resource.Error -> _uiState.update {
                    it.copy(isLoading = false, errorMessage = result.message)
                }
                else -> _uiState.update { it.copy(isLoading = false, errorMessage = null) }
            }
        }
    }

    private fun observeUmkList() {
        combine(
            searchQueryFlow.debounce(Constants.SEARCH_DEBOUNCE_MS).distinctUntilChanged(),
            _uiState
        ) { query, state -> Triple(query, state.selectedProvince, state.selectedRegency) to state.sortOrder }
            .distinctUntilChanged()
            .flatMapLatest { (filters, sortOrder) ->
                val (query, province, regency) = filters
                getUmkListUseCase(query, province, regency, sortOrder)
            }
            .catch { throwable ->
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = throwable.message ?: "Terjadi kesalahan")
                }
            }
            .onEach { list ->
                _uiState.update { it.copy(umkList = list, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun observeFilterOptions() {
        getProvincesUseCase()
            .onEach { provinces -> _uiState.update { it.copy(provinces = provinces) } }
            .launchIn(viewModelScope)

        _uiState
            .map { it.selectedProvince }
            .distinctUntilChanged()
            .flatMapLatest { province -> getRegenciesUseCase(province) }
            .onEach { regencies -> _uiState.update { it.copy(regencies = regencies) } }
            .launchIn(viewModelScope)
    }

    private fun observeSearchHistory() {
        getSearchHistoryUseCase()
            .onEach { history -> _uiState.update { it.copy(searchHistory = history) } }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        searchQueryFlow.value = query
    }

    fun onSearchActiveChange(active: Boolean) {
        _uiState.update { it.copy(isSearchActive = active) }
    }

    fun onSearchSubmit(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch { saveSearchHistoryUseCase(query) }
        _uiState.update { it.copy(isSearchActive = false) }
    }

    fun onHistoryItemClick(query: String) {
        onSearchQueryChange(query)
        onSearchSubmit(query)
    }

    fun onHistoryItemRemove(query: String) {
        viewModelScope.launch { deleteSearchHistoryItemUseCase(query) }
    }

    fun onClearHistory() {
        viewModelScope.launch { clearSearchHistoryUseCase() }
    }

    fun onProvinceSelected(province: String) {
        _uiState.update { it.copy(selectedProvince = province, selectedRegency = "") }
    }

    fun onRegencySelected(regency: String) {
        _uiState.update { it.copy(selectedRegency = regency) }
    }

    fun onSortOrderSelected(sortOrder: SortOrder) {
        _uiState.update { it.copy(sortOrder = sortOrder) }
    }

    fun onResetFilter() {
        _uiState.update {
            it.copy(selectedProvince = "", selectedRegency = "", sortOrder = SortOrder.NAME_ASC)
        }
    }

    fun onToggleFavorite(umk: Umk) {
        viewModelScope.launch { toggleFavoriteUseCase(umk) }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            when (val result = refreshUmkDataUseCase()) {
                is Resource.Error -> _uiState.update {
                    it.copy(isRefreshing = false, snackbarMessage = result.message)
                }
                else -> _uiState.update {
                    it.copy(isRefreshing = false, snackbarMessage = "Data berhasil diperbarui")
                }
            }
        }
    }

    fun onRetry() {
        loadInitialData()
    }

    fun onSnackbarShown() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}

