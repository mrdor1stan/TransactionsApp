package com.example.transactionsapp.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.transactionsapp.MainDispatcherRule
import com.example.transactionsapp.data.RatesPreferencesRepository
import com.example.transactionsapp.data.TransactionsRepository
import com.github.javafaker.Faker
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class MainScreenViewModelTest {
    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    val transactionsRepository = mockk<TransactionsRepository>()
    val ratesPreferencesRepository = mockk<RatesPreferencesRepository>()
    val faker = Faker()
    lateinit var viewModel: MainScreenViewModel
    val rate = 32.0 to LocalDateTime.now()
    @Before
    fun setup() {
        every { ratesPreferencesRepository.rate } returns MutableStateFlow(rate)
        every { transactionsRepository.getBalance() } returns MutableStateFlow(0.0)
        viewModel = MainScreenViewModel(transactionsRepository, ratesPreferencesRepository)
    }

    @Test
    fun onViewModelCreated_inputIsEmpty() {
        assertEquals("", viewModel.input.value)
    }

    @Test
    fun inputUpdatesCorrectly() {
        val newInput = faker.address().fullAddress()
        viewModel.updateInput(newInput)
        assertEquals(newInput, viewModel.input.value)
    }

    @Test
    fun validateInput_onIncorrectInput_returnsFalse() {
        viewModel.updateInput(faker.animal().name())
        assertFalse(viewModel.validateInput())
    }

    @Test
    fun validateInput_onCorrectInput_returnsTrue() {
        viewModel.updateInput(faker.number().randomNumber().toString())
        assertTrue(viewModel.validateInput())
    }

    @Test
    fun onUpdatesInputCall_stateUpdates() {
        val newInput = faker.lorem().word()
        viewModel.updateInput(newInput)
        assertEquals(newInput, viewModel.input.value)
    }

    @Test
    fun requireTopUpScreen_setsTrue() {
        viewModel.requireTopUpScreen(true)
        assertTrue(viewModel.uiState.value.topUpScreenRequired)
    }
    @Test
    fun requireTopUpScreen_setsFalse() {
        viewModel.requireTopUpScreen(false)
        assertFalse(viewModel.uiState.value.topUpScreenRequired)
    }
}