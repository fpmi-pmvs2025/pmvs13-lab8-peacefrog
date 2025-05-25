# Guess the Car Brand Game

A fun Android game where players guess car brands from their logos.

## Project Structure

```
guess_the_car/
├── app/                    # Android app code
├── data/                   # Dataset and generated files
│   ├── Car_Logo_Dataset/   # Car logo images
│   └── cars.json          # Generated JSON file
├── generate_cars_json.py   # Script to generate JSON
└── README.md              # This file
```

## Setup Instructions

1. **Dataset Preparation**:
   - Download the car logo dataset from Kaggle
   - Create a `data` directory in the project root
   - Extract the dataset to `data/Car_Logo_Dataset`
   - Run the Python script to generate the JSON file:
     ```bash
     python generate_cars_json.py
     ```
   - The script will create `data/cars.json`

2. **Update the App**:
   - Open `app/src/main/java/com/example/guess_the_car/data/repository/CarRepository.kt`
   - Replace the URL in the `refreshCars()` method with your actual GitHub raw JSON file URL:
     ```kotlin
     val cars = apiService.getCars("https://raw.githubusercontent.com/YOUR_USERNAME/guess_the_car/main/data/cars.json")
     ```

3. **Run the App**:
   - The app will automatically:
     - Load the car data from the JSON file
     - Store it in the local database
     - Start the game with the first question

## Game Rules

- The game shows a car logo and 4 possible brand names
- Select the correct brand to earn a point
- The game continues until you make a mistake or a brand is repeated
- Enter your name when the game ends to save your score
- Try to beat your high score!

## Development

- Built with Jetpack Compose
- Uses Room for local database
- Implements MVVM architecture
- Uses Hilt for dependency injection 