package com.example.guess_the_car

import org.junit.Test
import org.junit.Assert.*
import com.example.guess_the_car.ui.game.GameViewModel
import com.example.guess_the_car.data.repository.CarRepository

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun testGameInitialization() {
        val viewModel = GameViewModel(CarRepository(null, null))
        assertNotNull(viewModel)
    }

    @Test
    fun testScoreCalculation() {
        val viewModel = GameViewModel(CarRepository(null, null))
        // Add score calculation test when implemented
        assertTrue(true)
    }
}
