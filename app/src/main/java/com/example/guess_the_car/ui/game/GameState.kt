package com.example.guess_the_car.ui.game

import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore

sealed class    GameState {
    data object Loading : GameState()
    data class Error(val message: String) : GameState()
    data class Playing(
        val currentCar: Car,
        val options: List<String>,
        val score: Int = 0,
        val playerName: String = "",
        val highScores: List<PlayerScore> = emptyList()
    ) : GameState()
    data class GameOver(
        val finalScore: Int,
        val playerName: String = "",
        val highScores: List<PlayerScore> = emptyList()
    ) : GameState()
} 