package com.kurbatov.appcbt.domain

import javax.inject.Inject

class EditShopItemUseCase@Inject constructor(
    private val shopListRepository: ShopListRepository
) {
    suspend fun editShopItem(shopItem: ShopItem){
        shopListRepository.editShopItem(shopItem)
    }
}