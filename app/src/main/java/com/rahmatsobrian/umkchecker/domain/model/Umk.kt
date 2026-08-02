package com.rahmatsobrian.umkchecker.domain.model

/**
 * Pure domain representation of a single regional minimum wage (UMK/UMP) record.
 * This model has no dependency on Room, Retrofit, or any framework — the UI and
 * business logic layers only ever depend on this class.
 */
data class Umk(
    val id: Long,
    val regionName: String,
    val provinceName: String,
    val amount: Long,
    val year: Int,
    val isActive: Boolean,
    val isProvinceLevel: Boolean,
    val isFavorite: Boolean = false
)

enum class SortOrder(val label: String) {
    NAME_ASC("Nama A-Z"),
    NAME_DESC("Nama Z-A"),
    AMOUNT_DESC("UMK Tertinggi"),
    AMOUNT_ASC("UMK Terendah")
}
