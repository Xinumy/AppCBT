package com.kurbatov.appcbt.domain

data class ShopItem(
    val time: String,
    val situation: String,
    val emotion: String,
    val thought: String,
    val sensations: String,
    val actions: String,
    val answer: String,
    val distortionsList: List<String>,
    val enabled: Boolean,
    var id: Int = UNDEFINED_ID
){
    companion object {
        const val UNDEFINED_ID = 0
    }
}
