package com.example.guess_the_car.data.local

import androidx.room.*
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import kotlinx.coroutines.flow.Flow

@Dao
interface CarDao {
    @Query("SELECT * FROM cars")
    fun getAllCars(): Flow<List<Car>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCar(car: Car)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCars(cars: List<Car>)

    @Query("SELECT * FROM cars ORDER BY RANDOM() LIMIT 4")
    suspend fun getRandomCars(): List<Car>

    @Query("DELETE FROM cars")
    suspend fun deleteAllCars()

    @Query("SELECT * FROM cars WHERE brand = :brand LIMIT 1")
    suspend fun getCarByBrand(brand: String): Car?

    @Insert
    suspend fun insertPlayerScore(playerScore: PlayerScore)

    @Query("SELECT * FROM player_scores ORDER BY score DESC LIMIT 10")
    fun getTopScores(): Flow<List<PlayerScore>>

    @Query("SELECT COUNT(*) FROM cars WHERE brand = :brand")
    suspend fun getBrandCount(brand: String): Int
} 