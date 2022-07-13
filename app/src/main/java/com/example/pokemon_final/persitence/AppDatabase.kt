package com.example.pokemon_final.persitence

dimport androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.pokedex.model.Pokemon
import com.pokedex.model.PokemonInfo

@Database(entities = [Pokemon::class, PokemonInfo::class], version = 1, exportSchema = true)
@TypeConverters(value = [TypeResponseConverter::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun pokemonDao(): PokemonDao
    abstract fun pokemonInfoDao(): PokemonInfoDao
}