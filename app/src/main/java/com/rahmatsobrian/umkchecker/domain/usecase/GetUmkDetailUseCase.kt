package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUmkDetailUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    operator fun invoke(id: Long): Flow<Umk?> = repository.observeUmkDetail(id)
}
