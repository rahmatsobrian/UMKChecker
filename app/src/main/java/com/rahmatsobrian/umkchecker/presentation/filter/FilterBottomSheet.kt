package com.rahmatsobrian.umkchecker.presentation.filter

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBottomSheet(
    sheetState: SheetState,
    provinces: List<String>,
    regencies: List<String>,
    selectedProvince: String,
    selectedRegency: String,
    selectedSortOrder: SortOrder,
    onProvinceSelected: (String) -> Unit,
    onRegencySelected: (String) -> Unit,
    onSortOrderSelected: (SortOrder) -> Unit,
    onReset: () -> Unit,
    onApply: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Filter & Urutkan",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Provinsi",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedProvince.isEmpty(),
                        onClick = { onProvinceSelected("") },
                        label = { Text(Constants.ALL_PROVINCES) }
                    )
                }
                items(provinces) { province ->
                    FilterChip(
                        selected = selectedProvince == province,
                        onClick = { onProvinceSelected(province) },
                        label = { Text(province) }
                    )
                }
            }

            Text(
                text = "Kota/Kabupaten",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                item {
                    FilterChip(
                        selected = selectedRegency.isEmpty(),
                        onClick = { onRegencySelected("") },
                        label = { Text(Constants.ALL_REGENCIES) }
                    )
                }
                items(regencies) { regency ->
                    FilterChip(
                        selected = selectedRegency == regency,
                        onClick = { onRegencySelected(regency) },
                        label = { Text(regency) }
                    )
                }
            }

            Text(
                text = "Urutkan",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(SortOrder.entries.toList()) { order ->
                    FilterChip(
                        selected = selectedSortOrder == order,
                        onClick = { onSortOrderSelected(order) },
                        label = { Text(order.label) }
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onReset,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Reset")
                }
                Button(
                    onClick = onApply,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Terapkan")
                }
            }
        }
    }
}
