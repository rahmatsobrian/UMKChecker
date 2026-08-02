package com.rahmatsobrian.umkchecker.presentation.common

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.presentation.theme.ActiveGreen
import com.rahmatsobrian.umkchecker.presentation.theme.ActiveGreenContainer
import com.rahmatsobrian.umkchecker.util.CurrencyFormatter

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun UmkListItem(
    umk: Umk,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Rounded.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(
                        text = umk.regionName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
                Text(
                    text = umk.provinceName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    modifier = Modifier.padding(start = 28.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 6.dp, start = 28.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Text(
                        text = CurrencyFormatter.format(umk.amount),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    StatusBadge(isActive = umk.isActive, year = umk.year)
                }
            }
            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (umk.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = if (umk.isFavorite) "Hapus dari favorit" else "Tambah ke favorit",
                    tint = if (umk.isFavorite) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun StatusBadge(isActive: Boolean, year: Int, modifier: Modifier = Modifier) {
    val containerColor = if (isActive) ActiveGreenContainer else MaterialTheme.colorScheme.errorContainer
    val contentColor = if (isActive) ActiveGreen else MaterialTheme.colorScheme.error
    Surface(
        modifier = modifier.semantics { contentDescription = if (isActive) "Status aktif, tahun $year" else "Status tidak aktif, tahun $year" },
        shape = RoundedCornerShape(50),
        color = containerColor
    ) {
        Text(
            text = "${if (isActive) "Aktif" else "Nonaktif"} • $year",
            style = MaterialTheme.typography.labelSmall,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}
