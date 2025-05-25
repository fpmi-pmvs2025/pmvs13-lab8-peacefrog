package com.example.guess_the_car.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "player_scores")
data class PlayerScore(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val playerName: String,
    val score: Int,
    val timestamp: Long = System.currentTimeMillis()
) 