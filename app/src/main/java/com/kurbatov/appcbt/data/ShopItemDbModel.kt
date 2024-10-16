package com.kurbatov.appcbt.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shop_items")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val time: String,
    val situation: String,
    val emotion: String,
    val thought: String,
    val sensations: String,
    val actions: String,
    val answer: String,
    val distortionsList: List<String>,
    val enabled: Boolean
)
