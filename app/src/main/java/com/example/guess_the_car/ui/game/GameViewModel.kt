package com.example.guess_the_car.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guess_the_car.data.Car
import com.example.guess_the_car.data.CarRepository
import com.example.guess_the_car.data.PlayerScore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GameState(
    val currentCar: Car? = null,
    val score: Int = 0,
    val attempts: Int = 0,
    val maxAttempts: Int = 3,
    val isGameOver: Boolean = false,
    val message: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val playerName: String = "",
    val highScores: List<PlayerScore> = emptyList()
)

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: CarRepository
) : ViewModel() {

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    init {
        loadHighScores()
        startNewGame()
    }

    private fun loadHighScores() {
        viewModelScope.launch {
            try {
                val scores = repository.getTopScores()
                _gameState.update { it.copy(highScores = scores) }
            } catch (e: Exception) {
                _gameState.update { it.copy(error = "Failed to load high scores: ${e.message}") }
            }
        }
    }

    fun startNewGame() {
        viewModelScope.launch {
            try {
                _gameState.update { it.copy(isLoading = true) }
                val car = repository.getRandomCar()
                _gameState.update {
                    it.copy(
                        currentCar = car,
                        score = 0,
                        attempts = 0,
                        isGameOver = false,
                        message = "Guess the car brand!",
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _gameState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to start game: ${e.message}"
                    )
                }
            }
        }
    }

    fun checkGuess(guess: String) {
        val currentState = _gameState.value
        val currentCar = currentState.currentCar ?: return

        val isCorrect = guess.equals(currentCar.brand, ignoreCase = true)
        val newAttempts = currentState.attempts + 1
        val newScore = if (isCorrect) currentState.score + 10 else currentState.score
        val isGameOver = newAttempts >= currentState.maxAttempts

        _gameState.update {
            it.copy(
                attempts = newAttempts,
                score = newScore,
                isGameOver = isGameOver,
                message = if (isCorrect) "Correct! +10 points" else "Wrong! Try again"
            )
        }

        if (isGameOver) {
            saveScore()
        }
    }

    private fun saveScore() {
        val currentState = _gameState.value
        if (currentState.playerName.isNotBlank()) {
            viewModelScope.launch {
                try {
                    repository.insertScore(
                        PlayerScore(
                            playerName = currentState.playerName,
                            score = currentState.score
                        )
                    )
                    loadHighScores()
                } catch (e: Exception) {
                    _gameState.update { it.copy(error = "Failed to save score: ${e.message}") }
                }
            }
        }
    }

    fun setPlayerName(name: String) {
        _gameState.update { it.copy(playerName = name) }
    }

    fun skipCurrentCar() {
        viewModelScope.launch {
            try {
                val newCar = repository.getRandomCar()
                _gameState.update {
                    it.copy(
                        currentCar = newCar,
                        message = "Skipped! New car loaded"
                    )
                }
            } catch (e: Exception) {
                _gameState.update { it.copy(error = "Failed to load new car: ${e.message}") }
            }
        }
    }

    fun clearError() {
        _gameState.update { it.copy(error = null) }
    }
} 