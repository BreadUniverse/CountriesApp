package com.test.countriesapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.countriesapp.model.CountriesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CountriesEntity>)

    @Update
    suspend fun update(items: CountriesEntity)

    @Query("SELECT * FROM countries")
    suspend fun getCountries(): List<CountriesEntity>

    @Query("SELECT * FROM countries")
    fun getCountriesFlow(): Flow<List<CountriesEntity>>

}