package com.rahmatsobrian.umkchecker.presentation.favorite

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.presentation.common.EmptyState
import com.rahmatsobrian.umkchecker.presentation.common.FullScreenLoading
import com.rahmatsobrian.umkchecker.presentation.common.UmkListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    onUmkClick: (Umk) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Favorit Saya") }) }
    ) { padding ->
        when {
            uiState.isLoading -> FullScreenLoading(modifier = Modifier.padding(padding))
            uiState.isEmpty -> EmptyState(
                modifier = Modifier.padding(padding),
                icon = Icons.Rounded.FavoriteBorder,
                title = "Belum ada favorit",
                message = "Tap ikon hati pada daftar UMK untuk menyimpannya di sini."
            )
            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(uiState.favorites, key = { it.id }) { umk ->
                    UmkListItem(
                        umk = umk,
                        onClick = { onUmkClick(umk) },
                        onToggleFavorite = { viewModel.onToggleFavorite(umk) }
                    )
                }
            }
        }
    }
}
