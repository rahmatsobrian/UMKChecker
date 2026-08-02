package com.rahmatsobrian.umkchecker.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "umk_table",
    indices = [Index(value = ["regionName"]), Index(value = ["provinceName"])]
)
data class UmkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val regionName: String,
    val provinceName: String,
    val amount: Long,
    val year: Int,
    val isActive: Boolean,
    val isProvinceLevel: Boolean
)
