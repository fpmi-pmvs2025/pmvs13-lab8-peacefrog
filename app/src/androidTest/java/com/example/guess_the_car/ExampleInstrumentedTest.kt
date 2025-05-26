package com.example.guess_the_car

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testGameScreenElements() {
        // Launch the app
        composeTestRule.setContent {
            GuessTheCarTheme {
                GameScreen(viewModel = GameViewModel(CarRepository(null, null)))
            }
        }

        // Verify that basic UI elements are displayed
        composeTestRule.onNodeWithContentDescription("Car Image").assertIsDisplayed()
        composeTestRule.onNodeWithText("Guess the Car").assertIsDisplayed()
    }
}
