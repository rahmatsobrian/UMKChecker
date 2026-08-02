package com.rahmatsobrian.umkchecker.domain.usecase

import com.rahmatsobrian.umkchecker.domain.repository.UmkRepository
import com.rahmatsobrian.umkchecker.util.Resource
import javax.inject.Inject

class RefreshUmkDataUseCase @Inject constructor(
    private val repository: UmkRepository
) {
    suspend operator fun invoke(): Resource<Unit> = repository.refresh()
}
