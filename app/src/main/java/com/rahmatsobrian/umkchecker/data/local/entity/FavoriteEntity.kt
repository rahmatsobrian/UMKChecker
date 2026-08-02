package com.rahmatsobrian.umkchecker.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Stores which UMK records the user bookmarked. Kept as a separate table
 * (rather than a boolean column on [UmkEntity]) so that re-seeding or
 * refreshing the UMK dataset never wipes out the user's favorites.
 */
@Entity(tableName = "favorite_table")
data class FavoriteEntity(
    @PrimaryKey
    val umkId: Long
)
