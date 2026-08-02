package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProvincesUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    operator fun invoke(): Flow<List<String>> = repository.observeProvinces()
}

class GetRegenciesUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    operator fun invoke(province: String): Flow<List<String>> = repository.observeRegencies(province)
}
