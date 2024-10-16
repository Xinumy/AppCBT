package com.kurbatov.appcbt.domain

import javax.inject.Inject

class GetCurrentTimeUseCase @Inject constructor(
    private val shopListRepository: ShopListRepository
) {
    fun getCurrentTime() : String {
        return shopListRepository.getCurrentTime()
    }
}