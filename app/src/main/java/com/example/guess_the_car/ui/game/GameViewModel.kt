package com.example.guess_the_car.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import com.example.guess_the_car.data.repository.CarRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class GameViewModel(
    private val repository: CarRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow<GameState>(GameState.Loading)
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private var currentCar: Car? = null
    private var options: List<String> = emptyList()
    private var currentScore = 0
    private var playerName = ""

    init {
        loadHighScores()
        startNewGame()
    }

    private fun loadHighScores() {
        viewModelScope.launch {
            try {
                repository.getTopScores().collect { scores ->
                    updateGameState { currentState ->
                        when (currentState) {
                            is GameState.Playing -> currentState.copy(highScores = scores)
                            is GameState.GameOver -> currentState.copy(highScores = scores)
                            else -> currentState
                        }
                    }
                }
            } catch (e: Exception) {
                _gameState.value = GameState.Error("Failed to load high scores: ${e.message}")
            }
        }
    }

    fun startNewGame() {
        viewModelScope.launch {
            _gameState.value = GameState.Loading
            currentScore = 0
            try {
                repository.getAllCars().collect { cars ->
                    if (cars.isEmpty()) {
                        _gameState.value = GameState.Error("No cars available")
                        return@collect
                    }
                    selectNewCar(cars)
                }
            } catch (e: Exception) {
                _gameState.value = GameState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun selectNewCar(cars: List<Car>) {
        currentCar = cars.random()
        options = cars.map { it.brand }.distinct().shuffled().take(4)
        if (currentCar?.brand !in options) {
            options = options.dropLast(1) + (currentCar?.brand ?: "")
        }
        options = options.shuffled()
        _gameState.value = GameState.Playing(
            currentCar = currentCar!!,
            options = options,
            score = currentScore,
            playerName = playerName
        )
    }

    fun checkAnswer(selectedBrand: String) {
        val car = currentCar ?: return
        if (selectedBrand == car.brand) {
            currentScore++
            viewModelScope.launch {
                repository.getAllCars().collect { cars ->
                    selectNewCar(cars)
                }
            }
        } else {
            _gameState.value = GameState.GameOver(
                finalScore = currentScore,
                playerName = playerName
            )
            saveScore()
        }
    }

    private fun saveScore() {
        if (playerName.isNotBlank()) {
            viewModelScope.launch {
                try {
                    repository.saveScore(
                        PlayerScore(
                            playerName = playerName,
                            score = currentScore
                        )
                    )
                    loadHighScores()
                } catch (e: Exception) {
                    _gameState.value = GameState.Error("Failed to save score: ${e.message}")
                }
            }
        }
    }

    fun setPlayerName(name: String) {
        playerName = name
        updateGameState { currentState ->
            when (currentState) {
                is GameState.Playing -> currentState.copy(playerName = name)
                is GameState.GameOver -> currentState.copy(playerName = name)
                else -> currentState
            }
        }
    }

    fun skipCurrentCar() {
        viewModelScope.launch {
            try {
                repository.getAllCars().collect { cars ->
                    if (cars.isNotEmpty()) {
                        selectNewCar(cars)
                    }
                }
            } catch (e: Exception) {
                _gameState.value = GameState.Error("Failed to load new car: ${e.message}")
            }
        }
    }

    private fun updateGameState(update: (GameState) -> GameState) {
        _gameState.value = update(_gameState.value)
    }

    fun clearError() {
        _gameState.value = GameState.Loading
    }

    class Factory(private val repository: CarRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                return GameViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 