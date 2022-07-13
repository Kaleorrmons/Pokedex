package com.example.pokemon_final.mapper

import com.pokedex.model.PokemonErrorResponse
import com.sandwich.ApiErrorModelMapper
import com.sandwich.ApiResponse
import com.sandwich.message

object ErrorResponseMapper : ApiErrorModelMapper<PokemonErrorResponse> {

    override fun map(apiErrorResponse: ApiResponse.Failure.Error<*>): PokemonErrorResponse {
        return PokemonErrorResponse(apiErrorResponse.statusCode.code, apiErrorResponse.message())
    }
}