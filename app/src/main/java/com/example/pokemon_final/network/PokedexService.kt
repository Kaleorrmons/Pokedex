package com.example.pokemon_final.network

import com.pokedex.model.PokemonInfo
import com.pokedex.model.PokemonResponse
import com.sandwich.ApiResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokedexService {

    @GET("pokemon")
    suspend fun fetchPokemonList(
        @Query("limit") limit: Int = 20,
        @Query("offset") offset: Int = 0
    ): ApiResponse<PokemonResponse>

    @GET("pokemon/{name}")
    suspend fun fetchPokemonInfo(@Path("name") name: String): ApiResponse<PokemonInfo>
}