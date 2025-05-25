package com.example.guess_the_car.data.remote

data class CarResponse(
    val brand: String,
    val model: String,
    val imageUrl: String
) {
    fun toCar(): com.example.guess_the_car.data.model.Car {
        val fixedUrl = imageUrl.replace(
            "app/src/main/java/com/example/guess_the_car/data/Car_Logo_Dataset",
            "app/src/main/assets/Car_Logo_Dataset"
        )
        
        return com.example.guess_the_car.data.model.Car(
            brand = brand,
            model = model,
            imageUrl = fixedUrl
        )
    }
} 