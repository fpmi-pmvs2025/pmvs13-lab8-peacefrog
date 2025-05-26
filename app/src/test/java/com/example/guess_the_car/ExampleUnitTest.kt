package com.example.guess_the_car

import org.junit.Test
import org.junit.Assert.*
import com.example.guess_the_car.ui.game.GameViewModel
import com.example.guess_the_car.data.repository.CarRepository
import com.example.guess_the_car.data.local.CarDao
import com.example.guess_the_car.data.remote.CarApiService
import org.mockito.Mockito.mock
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito.`when`
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    private val mockCarDao = mock(CarDao::class.java)
    private val mockApiService = mock(CarApiService::class.java)
    private val repository = CarRepository(mockCarDao, mockApiService)

    @Test
    fun testGameInitialization() {
        val viewModel = GameViewModel(repository)
        assertNotNull(viewModel)
    }

    @Test
    fun testScoreCalculation() {
        val viewModel = GameViewModel(repository)
        // Add score calculation test when implemented
        assertTrue(true)
    }
}
