package com.example.guess_the_car.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.ui.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Score: ${gameState.score}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        if (gameState.isLoading) {
            CircularProgressIndicator()
        } else if (gameState.isGameOver) {
            GameOverScreen(
                score = gameState.score,
                playerName = gameState.playerName,
                onNameChange = viewModel::updatePlayerName,
                onSaveScore = {
                    viewModel.savePlayerScore()
                    viewModel.restartGame()
                }
            )
        } else {
            gameState.currentCar?.let { car ->
                AsyncImage(
                    model = car.imageUrl,
                    contentDescription = "Car image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.height(32.dp))

                gameState.options.forEach { option ->
                    AnswerButton(
                        car = option,
                        onClick = { viewModel.checkAnswer(option) }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        gameState.error?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Composable
fun GameOverScreen(
    score: Int,
    playerName: String,
    onNameChange: (String) -> Unit,
    onSaveScore: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Game Over!",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Your score: $score",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            value = playerName,
            onValueChange = onNameChange,
            label = { Text("Enter your name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = onSaveScore,
            enabled = playerName.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Score")
        }
    }
}

@Composable
fun AnswerButton(
    car: Car,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(text = car.brand)
    }
} 