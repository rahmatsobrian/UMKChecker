package com.rahmatsobrian.umkchecker.presentation.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.usecase.GetFavoritesUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FavoriteUiState(
    val isLoading: Boolean = true,
    val favorites: List<Umk> = emptyList()
) {
    val isEmpty: Boolean get() = !isLoading && favorites.isEmpty()
}

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoriteUiState())
    val uiState: StateFlow<FavoriteUiState> = _uiState.asStateFlow()

    init {
        getFavoritesUseCase()
            .catch { _uiState.update { state -> state.copy(isLoading = false) } }
            .onEach { list -> _uiState.update { it.copy(isLoading = false, favorites = list) } }
            .launchIn(viewModelScope)
    }

    fun onToggleFavorite(umk: Umk) {
        viewModelScope.launch { toggleFavoriteUseCase(umk) }
    }
}
