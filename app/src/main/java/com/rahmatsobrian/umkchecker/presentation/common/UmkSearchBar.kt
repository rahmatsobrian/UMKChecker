package com.rahmatsobrian.umkchecker.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahmatsobrian.umkchecker.domain.model.SearchHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UmkSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    onSearch: (String) -> Unit,
    history: List<SearchHistoryItem>,
    onHistoryItemClick: (String) -> Unit,
    onHistoryItemRemove: (String) -> Unit,
    onClearHistory: () -> Unit,
    modifier: Modifier = Modifier
) {
    SearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = onSearch,
        active = active,
        onActiveChange = onActiveChange,
        modifier = modifier,
        placeholder = { Text("Cari kota atau kabupaten...") },
        leadingIcon = {
            Icon(imageVector = Icons.Rounded.Search, contentDescription = "Cari")
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = "Hapus pencarian")
                }
            }
        },
        colors = SearchBarDefaults.colors()
    ) {
        if (history.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Riwayat pencarian",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Hapus semua",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { onClearHistory() }
                )
            }
            history.forEach { item ->
                ListItem(
                    headlineContent = { Text(item.query) },
                    leadingContent = {
                        Icon(imageVector = Icons.Rounded.History, contentDescription = null)
                    },
                    trailingContent = {
                        IconButton(onClick = { onHistoryItemRemove(item.query) }) {
                            Icon(imageVector = Icons.Rounded.Close, contentDescription = "Hapus")
                        }
                    },
                    modifier = Modifier.clickable { onHistoryItemClick(item.query) }
                )
            }
        } else {
            Text(
                text = "Mulai ketik untuk mencari daerah",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
