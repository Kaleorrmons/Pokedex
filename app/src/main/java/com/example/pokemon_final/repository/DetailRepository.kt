package com.example.pokemon_final.repository

import androidx.annotation.WorkerThread
import com.pokedex.mapper.ErrorResponseMapper
import com.pokedex.model.PokemonErrorResponse
import com.pokedex.model.PokemonInfo
import com.pokedex.network.PokedexClient
import com.pokedex.persistence.PokemonInfoDao
import com.sandwich.ApiResponse
import com.sandwich.map
import com.sandwich.onError
import com.sandwich.onException
import com.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

class DetailRepository @Inject constructor(
    private val pokedexClient: PokedexClient,
    private val pokemonInfoDao: PokemonInfoDao,
    private val ioDispatcher: CoroutineDispatcher
) : Repository {

    @WorkerThread
    fun fetchPokemonInfo(
        name: String,
        onComplete: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val pokemonInfo = pokemonInfoDao.getPokemonInfo(name)
        if (pokemonInfo == null) {
            val response = pokedexClient.fetchPokemonInfo(name = name)
            response.suspendOnSuccess {
                pokemonInfoDao.insertPokemonInfo(data)
                emit(data)
            }
                // handles the case when the API request gets an error response.
                // e.g., internal server error.
                .onError {
                    map(ErrorResponseMapper) { onError("[Code: $code]: $message") }
                }
                // handles the case when the API request gets an exception response.
                // e.g., network connection error.
                .onException { onError(message) }
        } else {
            emit(pokemonInfo)
        }
    }.onCompletion { onComplete() }.flowOn(ioDispatcher)
}