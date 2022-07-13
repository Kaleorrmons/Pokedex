package com.example.pokemon_final.details

import androidx.databinding.Bindable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.bindables.BindingViewModel
import com.bindables.asBindingProperty
import com.bindables.bindingProperty
import com.pokedex.model.PokemonInfo
import com.pokedex.repository.DetailRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

class DetailViewModel @AssistedInject constructor(
    detailRepository: DetailRepository,
    @Assisted private val pokemonName: String
) : BindingViewModel() {

    @get:Bindable
    var isLoading: Boolean by bindingProperty(true)
        private set

    @get:Bindable
    var toastMessage: String? by bindingProperty(null)
        private set

    private val pokemonInfoFlow: Flow<PokemonInfo?> = detailRepository.fetchPokemonInfo(
        name = pokemonName,
        onComplete = { isLoading = false },
        onError = { toastMessage = it }
    )

    @get:Bindable
    val pokemonInfo: PokemonInfo? by pokemonInfoFlow.asBindingProperty(viewModelScope, null)

    init {
        Timber.d("init DetailViewModel")
    }

    @dagger.assisted.AssistedFactory
    interface AssistedFactory {
        fun create(pokemonName: String): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            pokemonName: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(pokemonName) as T
            }
        }
    }
}