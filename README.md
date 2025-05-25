# Guess the Car Brand Game

## Description
Guess the Car Brand is an engaging Android game that tests players' knowledge of car brands through their logos. The game presents players with car logos and challenges them to identify the correct brand from multiple choices. Built with modern Android development practices, it features a clean architecture, local score tracking, and an intuitive user interface. The application uses Room database for data persistence and Jetpack Compose for a modern, responsive UI.

## Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/guess-the-car.git
   ```

2. Dataset Preparation:
   - Download the car logo dataset from Kaggle
   - Create a `data` directory in the project root
   - Extract the dataset to `data/Car_Logo_Dataset`
   - Run the Python script to generate the JSON file:
     ```bash
     python generate_cars_json.py
     ```

3. Open the project in Android Studio
4. Sync the project with Gradle files
5. Build and run the application

## Usage
1. Launch the application
2. The game will automatically load car data and start with the first question
3. For each round:
   - A car logo will be displayed
   - Four possible brand names will be shown
   - Select the correct brand to earn a point
   - The game continues until you make a mistake or a brand is repeated
4. When the game ends:
   - Enter your name to save your score
   - View your high score
   - Start a new game

## Contributing
### Team Members
- TToH4uk - Project Lead, Architecture Design, Backend Development
- mbugzy - UI/UX Development, Testing and Quality Assurance

### Implemented Tasks
- TToH4uk:
  - Architecture Design and Implementation
  - Database Implementation with Room
  - Game Logic and State Management
  - Project Management and Documentation

- mbugzy:
  - UI/UX Development with Jetpack Compose
  - Testing and Quality Assurance
  - User Interface Design
  - Performance Optimization
