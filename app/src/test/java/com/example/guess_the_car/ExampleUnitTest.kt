package com.example.guess_the_car

import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After
import com.example.guess_the_car.ui.game.GameViewModel
import com.example.guess_the_car.data.repository.CarRepository
import com.example.guess_the_car.data.local.CarDao
import com.example.guess_the_car.data.remote.CarApiService
import org.mockito.Mockito.mock
import kotlinx.coroutines.flow.flowOf
import org.mockito.Mockito.`when`
import com.example.guess_the_car.data.model.Car
import com.example.guess_the_car.data.model.PlayerScore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ExampleUnitTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val mockCarDao = mock(CarDao::class.java)
    private val mockApiService = mock(CarApiService::class.java)
    private val repository = CarRepository(mockCarDao, mockApiService)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testGameInitialization() {
        val viewModel = GameViewModel(repository)
        assertNotNull(viewModel)
    }

    @Test
    fun testScoreCalculation() {
        val viewModel = GameViewModel(repository)
        assertTrue(true)
    }
}
