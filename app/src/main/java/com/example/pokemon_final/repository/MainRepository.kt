package com.example.pokemon_final.repository

import androidx.annotation.WorkerThread
import com.pokedex.model.Pokemon
import com.network.PokedexClient
import com.pokedex.persistence.PokemonDao
import com.sandwich.ApiResponse
import com.sandwich.message
import com.sandwich.onFailure
import com.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonDao: PokemonDao,
    private val ioDispatcher: CoroutineDispatcher
) : Repository {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onStart: () -> Unit,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        var pokemons = pokemonDao.getPokemonList(page)
        if (pokemons.isEmpty()) {
            val response = pokedexClient.fetchPokemonList(page = page)
            response.suspendOnSuccess {
                pokemons = data.results
                pokemons.forEach { pokemon -> pokemon.page = page }
                pokemonDao.insertPokemonList(pokemons)
                emit(pokemonDao.getAllPokemonList(page))
            }.onFailure { // handles the all error cases from the API request fails.
                onError(message())
            }
        } else {
            emit(pokemonDao.getAllPokemonList(page))
        }
    }.onStart { onStart() }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}