package com.rahmatsobrian.umkchecker.data

import com.rahmatsobrian.umkchecker.data.dto.UmkDto
import com.rahmatsobrian.umkchecker.data.local.entity.UmkEntity
import com.rahmatsobrian.umkchecker.data.local.entity.UmkWithFavorite
import com.rahmatsobrian.umkchecker.domain.model.Umk

fun UmkDto.toEntity(): UmkEntity = UmkEntity(
    regionName = regionName,
    provinceName = provinceName,
    amount = amount,
    year = year,
    isActive = isActive,
    isProvinceLevel = isProvinceLevel
)

fun UmkWithFavorite.toDomain(): Umk = Umk(
    id = umk.id,
    regionName = umk.regionName,
    provinceName = umk.provinceName,
    amount = umk.amount,
    year = umk.year,
    isActive = umk.isActive,
    isProvinceLevel = umk.isProvinceLevel,
    isFavorite = isFavorite
)
