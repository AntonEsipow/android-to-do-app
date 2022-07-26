package com.bigtoapp.todo.ui.category.color

import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.math.roundToInt

class CustomColorPickerViewModel: ViewModel() {

    data class ColorViewState(
        val red: Int = 0,
        val green: Int = 0,
        val blue: Int = 0,
        private val categoryName: String = ""
    ) {
        fun getFormattedTitle(): String {
            return "$categoryName ($red, $green, $blue)"
        }
    }

    private lateinit var categoryName: String

    private val _colorViewStateLiveData = MutableLiveData<ColorViewState>()
    val colorViewStateLiveData: LiveData<ColorViewState> = _colorViewStateLiveData

    fun setCategoryColorName(categoryColor: Int, categoryName: String, colorCallback: (Int, Int, Int) -> Unit) {
        this.categoryName = categoryName

        val color = Color.valueOf(categoryColor)
        val redValue = (color.red() * 255).roundToInt()
        val greenValue = (color.green() * 255).roundToInt()
        val blueValue = (color.blue() * 255).roundToInt()

        colorCallback(redValue, greenValue, blueValue)

        _colorViewStateLiveData.postValue(ColorViewState(
            red = redValue,
            green = greenValue,
            blue = blueValue,
            categoryName = categoryName
        ))
    }

    fun onRedChange(red: Int) {
        val colorViewState = _colorViewStateLiveData.value ?: ColorViewState()
        _colorViewStateLiveData.postValue(colorViewState.copy(red = red))
    }

    fun onGreenChange(green: Int) {
        val colorViewState = _colorViewStateLiveData.value ?: ColorViewState()
        _colorViewStateLiveData.postValue(colorViewState.copy(green = green))
    }

    fun onBlueChange(blue: Int) {
        val colorViewState = _colorViewStateLiveData.value ?: ColorViewState()
        _colorViewStateLiveData.postValue(colorViewState.copy(blue = blue))
    }
}