package com.rahmatsobrian.umkchecker.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.usecase.GetUmkDetailUseCase
import com.rahmatsobrian.umkchecker.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DetailUiState(
    val isLoading: Boolean = true,
    val umk: Umk? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class DetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getUmkDetailUseCase: GetUmkDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val umkId: Long = checkNotNull(savedStateHandle["umkId"])

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    init {
        getUmkDetailUseCase(umkId)
            .catch { throwable ->
                _uiState.update { it.copy(isLoading = false, errorMessage = throwable.message) }
            }
            .onEach { umk ->
                _uiState.update { it.copy(isLoading = false, umk = umk, errorMessage = if (umk == null) "Data tidak ditemukan" else null) }
            }
            .launchIn(viewModelScope)
    }

    fun onToggleFavorite() {
        val current = _uiState.value.umk ?: return
        viewModelScope.launch { toggleFavoriteUseCase(current) }
    }
}
