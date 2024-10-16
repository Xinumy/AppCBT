package com.kurbatov.appcbt.data

import androidx.room.TypeConverter
import com.kurbatov.appcbt.domain.ShopItem
import javax.inject.Inject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ShopListMapper @Inject constructor(){

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        id = shopItem.id,
        time = shopItem.time,
        situation = shopItem.situation,
        emotion = shopItem.emotion,
        thought = shopItem.thought,
        sensations = shopItem.sensations,
        actions = shopItem.actions,
        answer = shopItem.answer,
        distortionsList = shopItem.distortionsList,
        enabled = shopItem.enabled
    )

    fun mapDbModelToEntity(shopItemDbModel: ShopItemDbModel) = ShopItem(
        id = shopItemDbModel.id,
        time = shopItemDbModel.time,
        situation = shopItemDbModel.situation,
        emotion = shopItemDbModel.emotion,
        thought = shopItemDbModel.thought,
        sensations = shopItemDbModel.sensations,
        actions = shopItemDbModel.actions,
        answer = shopItemDbModel.answer,
        distortionsList = shopItemDbModel.distortionsList,
        enabled = shopItemDbModel.enabled
    )

    fun mapListDbModelToListEntity(list: List<ShopItemDbModel>) = list.map{
        mapDbModelToEntity(it)
    }


    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType)
    }
}