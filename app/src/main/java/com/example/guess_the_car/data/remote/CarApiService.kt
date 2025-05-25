package com.example.guess_the_car.data.remote

import retrofit2.http.GET
import retrofit2.http.Url

interface CarApiService {
    @GET
    suspend fun getCars(@Url url: String): List<CarResponse>
}

data class CarResponse(
    val brand: String,
    val model: String,
    val imageUrl: String
) 