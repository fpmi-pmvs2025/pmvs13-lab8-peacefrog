package com.example.guess_the_car.data.repository

import com.example.guess_the_car.data.local.CarDao
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import com.example.guess_the_car.data.remote.CarApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CarRepository @Inject constructor(
    private val carDao: CarDao,
    private val apiService: CarApiService
) {
    fun getAllCars(): Flow<List<Car>> = carDao.getAllCars()

    suspend fun refreshCars() {
        try {
            // Using GitHub raw content URL for the JSON file
            val cars = apiService.getCars("https://raw.githubusercontent.com/YOUR_USERNAME/guess_the_car/main/data/cars.json").map { response ->
                Car(
                    brand = response.brand,
                    model = response.model,
                    imageUrl = response.imageUrl
                )
            }
            carDao.insertCars(cars)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getRandomCars(): List<Car> = carDao.getRandomCars()

    suspend fun savePlayerScore(playerScore: PlayerScore) {
        carDao.insertPlayerScore(playerScore)
    }

    fun getTopScores(): Flow<List<PlayerScore>> = carDao.getTopScores()

    suspend fun isBrandUsed(brand: String): Boolean {
        return carDao.getBrandCount(brand) > 0
    }
} 