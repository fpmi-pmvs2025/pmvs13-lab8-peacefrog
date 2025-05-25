package com.example.guess_the_car.data.remote

import android.util.Log

data class CarResponse(
    val brand: String,
    val model: String,
    val imageUrl: String
) {
    fun toCar(): com.example.guess_the_car.data.model.Car {
        // Extract the numbered folder name and filename
        val pathParts = imageUrl.split("\\")
        val folderName = pathParts[pathParts.size - 2] // e.g., "1-Volvo"
        val filename = pathParts.last() // e.g., "1-Volvo-1.png"
        
        // Construct the correct URL format
        val correctUrl = "https://raw.githubusercontent.com/fpmi-pmvs2025/pmvs13-lab8-peacefrog/main/app/src/main/assets/Car_Logo_Dataset/$folderName/$filename"
        Log.d("CarResponse", "Using URL: $correctUrl")
        
        return com.example.guess_the_car.data.model.Car(
            brand = brand,
            model = model,
            imageUrl = correctUrl
        )
    }
} 