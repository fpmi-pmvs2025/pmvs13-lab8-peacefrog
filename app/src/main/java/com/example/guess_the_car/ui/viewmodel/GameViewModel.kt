package com.example.guess_the_car.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import com.example.guess_the_car.data.repository.CarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameState(
    val currentCar: Car? = null,
    val options: List<Car> = emptyList(),
    val score: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGameOver: Boolean = false,
    val playerName: String = "",
    val showNameInput: Boolean = false,
    val usedBrands: Set<String> = emptySet()
)

class GameViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)
            try {
                // First, try to refresh cars from the API
                repository.refreshCars()
                // Then load the first question
                loadNewQuestion()
            } catch (e: Exception) {
                _gameState.value = _gameState.value.copy(
                    error = "Failed to load data: ${e.message}"
                )
            } finally {
                _gameState.value = _gameState.value.copy(isLoading = false)
            }
        }
    }

    fun loadNewQuestion() {
        viewModelScope.launch {
            _gameState.value = _gameState.value.copy(isLoading = true)
            try {
                val cars = repository.getRandomCars()
                if (cars.size >= 4) {
                    val correctCar = cars.random()
                    
                    // Check if brand was already used
                    if (_gameState.value.usedBrands.contains(correctCar.brand)) {
                        gameOver()
                        return@launch
                    }

                    _gameState.value = _gameState.value.copy(
                        currentCar = correctCar,
                        options = cars,
                        usedBrands = _gameState.value.usedBrands + correctCar.brand
                    )
                }
            } catch (e: Exception) {
                _gameState.value = _gameState.value.copy(
                    error = "Failed to load question: ${e.message}"
                )
            } finally {
                _gameState.value = _gameState.value.copy(isLoading = false)
            }
        }
    }

    fun checkAnswer(selectedCar: Car) {
        val currentState = _gameState.value
        val correctCar = currentState.currentCar

        if (correctCar?.brand == selectedCar.brand) {
            _gameState.value = currentState.copy(
                score = currentState.score + 1
            )
            loadNewQuestion()
        } else {
            gameOver()
        }
    }

    private fun gameOver() {
        _gameState.value = _gameState.value.copy(
            isGameOver = true,
            showNameInput = true
        )
    }

    fun savePlayerScore() {
        viewModelScope.launch {
            val currentState = _gameState.value
            if (currentState.playerName.isNotBlank()) {
                repository.savePlayerScore(
                    PlayerScore(
                        playerName = currentState.playerName,
                        score = currentState.score
                    )
                )
            }
        }
    }

    fun updatePlayerName(name: String) {
        _gameState.value = _gameState.value.copy(playerName = name)
    }

    fun restartGame() {
        _gameState.value = GameState()
        loadNewQuestion()
    }
} 