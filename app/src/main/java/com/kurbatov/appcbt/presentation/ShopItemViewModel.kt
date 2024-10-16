package com.kurbatov.appcbt.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kurbatov.appcbt.domain.AddShopItemUseCase
import com.kurbatov.appcbt.domain.EditShopItemUseCase
import com.kurbatov.appcbt.domain.GetCurrentTimeUseCase
import com.kurbatov.appcbt.domain.GetShopItemUseCase
import com.kurbatov.appcbt.domain.ShopItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class ShopItemViewModel @Inject constructor(
    private val getShopItemUseCase: GetShopItemUseCase,
    private val addShopItemUseCase: AddShopItemUseCase,
    private val editShopItemUseCase: EditShopItemUseCase,
    private val getCurrentTimeUseCase: GetCurrentTimeUseCase,
) : ViewModel() {

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItem = MutableLiveData<ShopItem>()
    val shopItem: LiveData<ShopItem>
        get() = _shopItem

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    fun getShopItem(shopItemId: Int) {
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItem(shopItemId)
            _shopItem.value = item
        }
    }

    fun getCurrentTime(): String = getCurrentTimeUseCase.getCurrentTime()

    fun addShopItem(
        inputTime:String?,
        inputSituation: String?,
        inputEmotion: String?,
        inputThought: String?,
        inputSensations: String?,
        inputActions: String?,
        inputAnswer: String?,
        inputDistortionsList: List<String>
    ) {
        val time = parseString(inputTime)
        val situation = parseString(inputSituation)
        val emotion = parseString(inputEmotion)
        val thought = parseString(inputThought)
        val sensations = parseString(inputSensations)
        val actions = parseString(inputActions)
        val answer = parseString(inputAnswer)
        //val fieldsValid = validateInput(name, count)
        //Log.d("SaveButton", fieldsValid.toString())
        viewModelScope.launch {
            val shopItem = ShopItem(
                time,
                situation,
                emotion,
                thought,
                sensations,
                actions,
                answer,
                inputDistortionsList,
                true
            )
            addShopItemUseCase.addShopItem(shopItem)
            finishWork()

        }
    }

    fun editShopItem(
        inputTime:String?,
        inputSituation: String?,
        inputEmotion: String?,
        inputThought: String?,
        inputSensations: String?,
        inputActions: String?,
        inputAnswer: String?,
        inputDistortionsList: List<String>
    ) {
        val time = parseString(inputTime)
        val situation = parseString(inputSituation)
        val emotion = parseString(inputEmotion)
        val thought = parseString(inputThought)
        val sensations = parseString(inputSensations)
        val actions = parseString(inputActions)
        val answer = parseString(inputAnswer)
        _shopItem.value?.let {
            viewModelScope.launch {
                val item = it.copy(
                    time,
                    situation,
                    emotion,
                    thought,
                    sensations,
                    actions,
                    answer,
                    inputDistortionsList,
                    true
                )
                editShopItemUseCase.editShopItem(item)
                finishWork()
            }
        }
    }

    private fun parseString(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun validateInput(name: String, count: Int): Boolean {
        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }
        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    public fun resetErrorInputName() {
        _errorInputName.value = false
    }

    public fun resetErrorInputCount() {
        _errorInputCount.value = false
    }

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }

}