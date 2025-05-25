package com.example.guess_the_car.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.guess_the_car.data.Car

@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    var guess by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Player Name Input
        if (gameState.playerName.isBlank()) {
            OutlinedTextField(
                value = guess,
                onValueChange = { guess = it },
                label = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(
                onClick = {
                    viewModel.setPlayerName(guess)
                    guess = ""
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Game")
            }
        } else {
            // Game UI
            Text(
                text = "Player: ${gameState.playerName}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Score: ${gameState.score}",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "Attempts: ${gameState.attempts}/${gameState.maxAttempts}",
                style = MaterialTheme.typography.titleMedium
            )

            // Car Image
            gameState.currentCar?.let { car ->
                // TODO: Add car image display
                Text(
                    text = "Car Image Placeholder",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            // Guess Input
            OutlinedTextField(
                value = guess,
                onValueChange = { guess = it },
                label = { Text("Enter car brand") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.checkGuess(guess)
                        guess = ""
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !gameState.isGameOver
                ) {
                    Text("Submit Guess")
                }

                Button(
                    onClick = {
                        viewModel.skipCurrentCar()
                        guess = ""
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !gameState.isGameOver
                ) {
                    Text("Skip")
                }
            }

            // Game Message
            Text(
                text = gameState.message,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // Game Over UI
            if (gameState.isGameOver) {
                Button(
                    onClick = { viewModel.startNewGame() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Play Again")
                }
            }

            // High Scores
            if (gameState.highScores.isNotEmpty()) {
                Text(
                    text = "High Scores",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
                gameState.highScores.forEach { score ->
                    Text(
                        text = "${score.playerName}: ${score.score}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Error Message
        gameState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Loading Indicator
        if (gameState.isLoading) {
            CircularProgressIndicator()
        }
    }
} 