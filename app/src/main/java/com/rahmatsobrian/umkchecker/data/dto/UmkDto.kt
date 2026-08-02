package com.rahmatsobrian.umkchecker.data.dto

import kotlinx.serialization.Serializable

/**
 * Mirrors the shape of `assets/umk_data.json`. Kept deliberately simple so the
 * dataset can be updated by hand or generated from an official source without
 * touching any app code — only this file's shape needs to stay in sync.
 */
@Serializable
data class UmkDto(
    val regionName: String,
    val provinceName: String,
    val amount: Long,
    val year: Int,
    val isActive: Boolean = true,
    val isProvinceLevel: Boolean = false
)

@Serializable
data class UmkDataset(
    val updatedAt: String,
    val source: String,
    val items: List<UmkDto>
)
