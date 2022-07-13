package com.example.pokemon_final.di

import com.pokedex.network.PokedexClient
import com.pokedex.persistence.PokemonDao
import com.pokedex.persistence.PokemonInfoDao
import com.pokedex.repository.DetailRepository
import com.pokedex.repository.MainRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.CoroutineDispatcher

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun provideMainRepository(
        pokedexClient: PokedexClient,
        pokemonDao: PokemonDao,
        coroutineDispatcher: CoroutineDispatcher
    ): MainRepository {
        return MainRepository(pokedexClient, pokemonDao, coroutineDispatcher)
    }

    @Provides
    @ViewModelScoped
    fun provideDetailRepository(
        pokedexClient: PokedexClient,
        pokemonInfoDao: PokemonInfoDao,
        coroutineDispatcher: CoroutineDispatcher
    ): DetailRepository {
        return DetailRepository(pokedexClient, pokemonInfoDao, coroutineDispatcher)
    }
}