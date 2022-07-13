package com.example.pokemon_final.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.bindables.BindingActivity
import com.bundler.bundleNonNull
import com.bundler.intentOf
import com.pokedex.R
import com.pokedex.databinding.ActivityDetailBinding
import com.pokedex.model.Pokemon
import com.transformationlayout.TransformationCompat
import com.transformationlayout.TransformationCompat.onTransformationEndContainerApplyParams
import com.transformationlayout.TransformationLayout
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailActivity : BindingActivity<ActivityDetailBinding>(R.layout.activity_detail) {

    @set:Inject
    internal lateinit var detailViewModelFactory: DetailViewModel.AssistedFactory

    @get:VisibleForTesting
    internal val viewModel: DetailViewModel by viewModels {
        DetailViewModel.provideFactory(detailViewModelFactory, pokemonItem.name)
    }

    private val pokemonItem: Pokemon by bundleNonNull(EXTRA_POKEMON)

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationEndContainerApplyParams(this)
        super.onCreate(savedInstanceState)
        binding {
            pokemon = pokemonItem
            vm = viewModel
        }
    }

    companion object {
        @VisibleForTesting
        internal const val EXTRA_POKEMON = "EXTRA_POKEMON"

        fun startActivity(transformationLayout: TransformationLayout, pokemon: Pokemon) =
            transformationLayout.context.intentOf<DetailActivity> {
                putExtra(EXTRA_POKEMON to pokemon)
                TransformationCompat.startActivity(transformationLayout, intent)
            }
    }
}
