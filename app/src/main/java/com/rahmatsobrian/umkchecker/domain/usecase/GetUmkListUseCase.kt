package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.model.SortOrder
import com.rahmatsobrian.umkchecker.domain.model.Umk
import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUmkListUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    operator fun invoke(
        query: String = "",
        province: String = "",
        regency: String = "",
        sortOrder: SortOrder = SortOrder.NAME_ASC
    ): Flow<List<Umk>> = repository.observeUmkList(query, province, regency, sortOrder)
}
