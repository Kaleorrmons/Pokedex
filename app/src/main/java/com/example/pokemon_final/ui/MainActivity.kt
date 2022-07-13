package com.example.pokemon_final.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.VisibleForTesting
import com.bindables.BindingActivity
import com.example.pokemon_final.R
import com.pokedex.databinding.ActivityMainBinding
import com.pokedex.ui.adapter.PokemonAdapter
import com.transformationlayout.onTransformationStartContainer
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BindingActivity<ActivityMainBinding>(R.layout.activity_main) {

    @get:VisibleForTesting
    internal val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        onTransformationStartContainer()
        super.onCreate(savedInstanceState)
        binding {
            adapter = PokemonAdapter()
            vm = viewModel
        }
    }
}
