package com.example.guess_the_car.ui.game

import androidx.compose.foundation.layout.*
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import android.util.Log
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.ui.game.GameState

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    modifier: Modifier = Modifier
) {
    val gameState by viewModel.gameState.collectAsStateWithLifecycle()

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
            is GameState.GameOver -> {
                val currentState = gameState as GameState.GameOver
                Text(
                    text = "Game Over! Final Score: ${currentState.finalScore}",
                    style = MaterialTheme.typography.headlineMedium
                )
                if (currentState.highScores.isNotEmpty()) {
                    Text(
                        text = "High Scores",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    currentState.highScores.forEach { score ->
                        Text(
                            text = "${score.playerName}: ${score.score}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
                Button(onClick = { viewModel.startNewGame() }) {
                    Text("Play Again")
                }
            }
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
        Log.d("GameScreen", "Attempting to load image from URL: ${currentCar.imageUrl}")
        
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(currentCar.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Car logo for ${currentCar.brand}",
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentScale = ContentScale.Fit,
            onError = { 
                Log.e("GameScreen", "Error loading image: ${it.result.throwable}")
                Log.e("GameScreen", "Error details: ${it.result.throwable.message}")
                Log.e("GameScreen", "Error cause: ${it.result.throwable.cause}")
            },
            onSuccess = { 
                Log.d("GameScreen", "Successfully loaded image")
            }
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