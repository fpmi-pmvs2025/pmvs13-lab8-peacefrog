package com.example.guess_the_car

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.guess_the_car.data.local.CarDatabase
import com.example.guess_the_car.data.remote.CarApiService
import com.example.guess_the_car.data.repository.CarRepository
import com.example.guess_the_car.ui.game.GameScreen
import com.example.guess_the_car.ui.game.GameViewModel
import com.example.guess_the_car.ui.theme.GuessTheCarTheme
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    private val repository by lazy {
        val database = CarDatabase.getDatabase(applicationContext)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://raw.githubusercontent.com/fpmi-pmvs2025/pmvs13-lab8-peacefrog/main/app/src/main/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val apiService = retrofit.create(CarApiService::class.java)
        CarRepository(database.carDao(), apiService)
    }

    private val viewModel: GameViewModel by viewModels {
        GameViewModel.Factory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuessTheCarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen(viewModel = viewModel)
                }
            }
        }
    }
}