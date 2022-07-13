package com.example.pokemon_final.repository

import app.cash.turbine.test
import com.mockitokotlin2.atLeastOnce
import com.mockitokotlin2.mock
import com.mockitokotlin2.verify
import com.mockitokotlin2.verifyNoMoreInteractions
import com.mockitokotlin2.whenever
import com.pokedex.MainCoroutinesRule
import com.pokedex.model.PokemonResponse
import com.pokedex.network.PokedexClient
import com.pokedex.network.PokedexService
import com.pokedex.persistence.PokemonDao
import com.pokedex.utils.MockUtil.mockPokemonList
import com.sandwich.ApiResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class MainRepositoryTest {

    private lateinit var repository: MainRepository
    private lateinit var client: PokedexClient
    private val service: PokedexService = mock()
    private val pokemonDao: PokemonDao = mock()

    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        client = PokedexClient(service)
        repository = MainRepository(client, pokemonDao, coroutinesRule.testDispatcher)
    }

    @Test
    fun fetchPokemonListFromNetworkTest() = runTest {
        val mockData =
            PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
        whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(emptyList())
        whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results)
        whenever(service.fetchPokemonList()).thenReturn(ApiResponse.of { Response.success(mockData) })

        repository.fetchPokemonList(
            page = 0,
            onStart = {},
            onComplete = {},
            onError = {}
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val expectItem = awaitItem()[0]
            assertEquals(expectItem.page, 0)
            assertEquals(expectItem.name, "bulbasaur")
            assertEquals(expectItem, mockPokemonList()[0])
            awaitComplete()
        }

        verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
        verify(service, atLeastOnce()).fetchPokemonList()
        verify(pokemonDao, atLeastOnce()).insertPokemonList(mockData.results)
        verifyNoMoreInteractions(service)
    }

    @Test
    fun fetchPokemonListFromDatabaseTest() = runTest {
        val mockData =
            PokemonResponse(count = 984, next = null, previous = null, results = mockPokemonList())
        whenever(pokemonDao.getPokemonList(page_ = 0)).thenReturn(mockData.results)
        whenever(pokemonDao.getAllPokemonList(page_ = 0)).thenReturn(mockData.results)

        repository.fetchPokemonList(
            page = 0,
            onStart = {},
            onComplete = {},
            onError = {}
        ).test(2.toDuration(DurationUnit.SECONDS)) {
            val expectItem = awaitItem()[0]
            assertEquals(expectItem.page, 0)
            assertEquals(expectItem.name, "bulbasaur")
            assertEquals(expectItem, mockPokemonList()[0])
            awaitComplete()
        }

        verify(pokemonDao, atLeastOnce()).getPokemonList(page_ = 0)
        verify(pokemonDao, atLeastOnce()).getAllPokemonList(page_ = 0)
    }
}