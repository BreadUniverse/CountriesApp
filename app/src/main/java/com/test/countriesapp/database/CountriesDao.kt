package com.test.countriesapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.CountriesModel
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

    @Query("SELECT * FROM countries WHERE id = :id")
    suspend fun getCountriesById(id:Long): CountriesEntity

    @Query("UPDATE countries SET isCheck = :isCheck WHERE id = :id")
    suspend fun updateIsCheckById(id:Long, isCheck:Boolean)

    @Query("UPDATE countries SET node = :node WHERE id = :id")
    suspend fun updateCommentById(id: Long, node: String)

}
