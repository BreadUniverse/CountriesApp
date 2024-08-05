package com.test.countriesapp.database.universities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.test.countriesapp.model.universities.UniversitiesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UniversitiesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<UniversitiesEntity>)

    @Update
    suspend fun update(items: UniversitiesEntity)

    @Query("SELECT * FROM universities")
    suspend fun getUniversities(): List<UniversitiesEntity>

    @Query("SELECT * FROM universities")
    fun getUniversitiesFlow(): Flow<List<UniversitiesEntity>>

    @Query("SELECT * FROM universities WHERE id = :id")
    suspend fun getUniversitiesById(id:Long): UniversitiesEntity

    @Query("""
        SELECT * FROM universities 
         WHERE country LIKE '%' || :countryName || '%'
    """)
    suspend fun findAllByCountryName(countryName: String): List<UniversitiesEntity>

}