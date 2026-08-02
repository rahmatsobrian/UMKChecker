package com.rahmatsobrian.umkchecker.data.local.entity

import androidx.room.Embedded

/**
 * Result of a LEFT JOIN between umk_table and favorite_table.
 * [isFavorite] is computed at query time via a `CASE WHEN favorite_table.umkId IS NOT NULL`.
 */
data class UmkWithFavorite(
    @Embedded
    val umk: UmkEntity,
    val isFavorite: Boolean
)
