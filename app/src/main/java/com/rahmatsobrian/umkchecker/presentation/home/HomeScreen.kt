package com.rahmatsobrian.umkchecker.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.presentation.common.EmptyState
import com.rahmatsobrian.umkchecker.presentation.common.ErrorState
import com.rahmatsobrian.umkchecker.presentation.common.FullScreenLoading
import com.rahmatsobrian.umkchecker.presentation.common.UmkListItem
import com.rahmatsobrian.umkchecker.presentation.common.UmkSearchBar
import com.rahmatsobrian.umkchecker.presentation.filter.FilterBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onUmkClick: (Umk) -> Unit,
    snackbarHostState: SnackbarHostState,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilterSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onSnackbarShown()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            UmkSearchBar(
                query = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                active = uiState.isSearchActive,
                onActiveChange = viewModel::onSearchActiveChange,
                onSearch = viewModel::onSearchSubmit,
                history = uiState.searchHistory,
                onHistoryItemClick = viewModel::onHistoryItemClick,
                onHistoryItemRemove = viewModel::onHistoryItemRemove,
                onClearHistory = viewModel::onClearHistory,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${uiState.umkList.size} daerah ditemukan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                BadgedBox(badge = {
                    if (uiState.hasActiveFilter) Badge()
                }) {
                    TextButton(onClick = { showFilterSheet = true }) {
                        Icon(
                            imageVector = Icons.Rounded.FilterList,
                            contentDescription = "Filter",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text("Filter")
                    }
                }
            }

            PullToRefreshBox(
                isRefreshing = uiState.isRefreshing,
                onRefresh = viewModel::onRefresh,
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    uiState.isLoading -> FullScreenLoading()
                    uiState.errorMessage != null -> ErrorState(
                        message = uiState.errorMessage.orEmpty(),
                        onRetry = viewModel::onRetry
                    )
                    uiState.isEmpty -> EmptyState(
                        title = "Tidak ada data ditemukan",
                        message = "Coba ubah kata kunci pencarian atau filter yang digunakan."
                    )
                    else -> LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.umkList, key = { it.id }) { umk ->
                            UmkListItem(
                                umk = umk,
                                onClick = { onUmkClick(umk) },
                                onToggleFavorite = { viewModel.onToggleFavorite(umk) }
                            )
                        }
                        item { Spacer(Modifier.height(72.dp)) }
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        FilterBottomSheet(
            sheetState = sheetState,
            provinces = uiState.provinces,
            regencies = uiState.regencies,
            selectedProvince = uiState.selectedProvince,
            selectedRegency = uiState.selectedRegency,
            selectedSortOrder = uiState.sortOrder,
            onProvinceSelected = viewModel::onProvinceSelected,
            onRegencySelected = viewModel::onRegencySelected,
            onSortOrderSelected = viewModel::onSortOrderSelected,
            onReset = viewModel::onResetFilter,
            onApply = { showFilterSheet = false },
            onDismiss = { showFilterSheet = false }
        )
    }
}
