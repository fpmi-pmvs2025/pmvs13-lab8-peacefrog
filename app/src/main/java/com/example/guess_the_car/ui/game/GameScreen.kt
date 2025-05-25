package com.example.guess_the_car.ui.game

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()
    var nameInput by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        when (gameState) {
            is GameState.Loading -> {
                CircularProgressIndicator()
            }
            is GameState.Error -> {
                Text(
                    text = (gameState as GameState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center
                )
                Button(onClick = { viewModel.startNewGame() }) {
                    Text("Try Again")
                }
            }
            is GameState.Playing -> {
                val currentState = gameState as GameState.Playing
                Text(
                    text = "Score: ${currentState.score}",
                    style = MaterialTheme.typography.headlineMedium
                )
                GameContent(
                    currentCar = currentState.currentCar,
                    options = currentState.options,
                    onOptionSelected = { viewModel.checkAnswer(it) }
                )
            }
            is GameState.EnterName -> {
                val score = (gameState as GameState.EnterName).score
                Text(
                    text = "Game Over!",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Your Score: $score",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Enter your name to save your score",
                    style = MaterialTheme.typography.titleMedium
                )
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    label = { Text("Your Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = {
                        if (nameInput.isNotBlank()) {
                            viewModel.setPlayerName(nameInput)
                            nameInput = "" // Clear the input after saving
                        }
                    },
                    enabled = nameInput.isNotBlank()
                ) {
                    Text("Save Score")
                }
            }
            is GameState.GameOver -> {
                val currentState = gameState as GameState.GameOver
                Text(
                    text = "Game Over!",
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = "Final Score: ${currentState.finalScore}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = if (currentState.isHighScore) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
                
                if (currentState.highScores.isNotEmpty()) {
                    Text(
                        text = "Leaderboard",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(currentState.highScores) { score ->
                            LeaderboardItem(score)
                        }
                    }
                }
                
                Button(
                    onClick = { viewModel.startNewGame() },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Play Again")
                }
            }
        }
    }
}

@Composable
private fun LeaderboardItem(score: PlayerScore) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = score.playerName,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = score.score.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun GameContent(
    currentCar: Car,
    options: List<String>,
    onOptionSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentCar.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Car logo for ${currentCar.brand}",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit
        )
        
        Text(
            text = "What car brand is this?",
            style = MaterialTheme.typography.titleLarge
        )

        options.forEach { option ->
            Button(
                onClick = { onOptionSelected(option) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(option)
            }
        }
    }
} 