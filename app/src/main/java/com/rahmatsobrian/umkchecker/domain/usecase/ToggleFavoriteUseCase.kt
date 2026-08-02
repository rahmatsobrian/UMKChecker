package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    suspend operator fun invoke(umk: Umk) = repository.toggleFavorite(umk)
}
