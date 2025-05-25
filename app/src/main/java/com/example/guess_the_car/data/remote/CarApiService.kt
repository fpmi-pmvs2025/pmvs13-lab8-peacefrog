package com.example.guess_the_car.data.remote

import retrofit2.http.GET

interface CarApiService {
    @GET("assets/cars.json")
    suspend fun getCars(): List<CarResponse>
}
