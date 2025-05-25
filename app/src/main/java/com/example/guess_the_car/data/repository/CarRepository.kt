package com.example.guess_the_car.data.repository

import android.content.Context
import android.util.Log
import com.example.guess_the_car.data.local.CarDao
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import com.example.guess_the_car.data.remote.CarApiService
import com.example.guess_the_car.data.remote.CarResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class CarRepository(
    private val carDao: CarDao,
    private val apiService: CarApiService
) {
    fun getAllCars(): Flow<List<Car>> = flow {
        // Clear existing data and fetch from API
        try {
            carDao.deleteAllCars()
            val cars = apiService.getCars().map { 
                val car = it.toCar()
                Log.d("CarRepository", "API car URL before saving: ${car.imageUrl}")
                car
            }
            carDao.insertCars(cars)
            emit(cars)
        } catch (e: Exception) {
            Log.e("CarRepository", "Error fetching cars: ${e.message}")
            throw e
        }
    }

    fun getTopScores(): Flow<List<PlayerScore>> = carDao.getTopScores()

    suspend fun saveScore(score: PlayerScore) {
        carDao.insertPlayerScore(score)
    }

    suspend fun refreshCars() {
        try {
            val cars = apiService.getCars().map { it.toCar() }
            carDao.insertCars(cars)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getRandomCars(): List<Car> = carDao.getRandomCars()

    suspend fun isBrandUsed(brand: String): Boolean {
        return carDao.getBrandCount(brand) > 0
    }

    companion object {
        @Volatile
        private var INSTANCE: CarRepository? = null

        fun getInstance(context: Context, carDao: CarDao, apiService: CarApiService): CarRepository {
            return INSTANCE ?: synchronized(this) {
                val instance = CarRepository(carDao, apiService)
                INSTANCE = instance
                instance
            }
        }
    }
} 