package com.example.guess_the_car.data.repository

import android.content.Context
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
        // First try to get cars from the database
        val localCars = carDao.getAllCars().first()
        if (localCars.isNotEmpty()) {
            emit(localCars)
        } else {
            // If no cars in database, fetch from API
            try {
                val cars = apiService.getCars().map { it.toCar() }
                carDao.insertCars(cars)
                emit(cars)
            } catch (e: Exception) {
                throw e
            }
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