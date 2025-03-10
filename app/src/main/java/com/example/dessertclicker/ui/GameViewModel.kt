package com.example.dessertclicker.ui

import androidx.lifecycle.ViewModel
import com.example.dessertclicker.data.Datasource.dessertList
import com.example.dessertclicker.data.GameUIState
import com.example.dessertclicker.model.Dessert
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(GameUIState())
    val uiState: StateFlow<GameUIState> = _uiState.asStateFlow()

    /**
     * Update dessert state on click
     */
    fun onDessertClicked() {
        _uiState.update {
            currentUIState ->
            val nextDessertId = determineDessertIndex(currentUIState.dessertsSold)
            currentUIState.copy(
                revenue = currentUIState.revenue + currentUIState.currentDessertPrice,
                dessertsSold = currentUIState.dessertsSold + 1,
                currentDessertIndex = nextDessertId,
                currentDessertPrice = dessertList[nextDessertId].price,
                currentDessertImageId = dessertList[nextDessertId].imageId
            )
        }
    }

    /**
     * Determine which dessert to show (return index)
     */
    private fun determineDessertIndex(
        dessertsSold: Int
    ): Int {
        var dessertId: Int = 0
        for (i in dessertList.indices) {
            if (dessertsSold >= dessertList[i].startProductionAmount) {
                dessertId = i
            } else {
                // The list of desserts is sorted by startProductionAmount. As you sell more desserts,
                // you'll start producing more expensive desserts as determined by startProductionAmount
                // We know to break as soon as we see a dessert who's "startProductionAmount" is greater
                // than the amount sold.
                break
            }
        }

        return dessertId
    }
}