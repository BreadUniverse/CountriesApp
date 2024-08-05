package com.test.countriesapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.test.countriesapp.database.universities.UniversitiesDao
import com.test.countriesapp.model.CountriesEntity
import com.test.countriesapp.model.universities.UniversitiesEntity

//аннотация, используемая в классе, чтобы указать, что это класс базы данных Room и определить основные параметры бд
// объявление абстрактного метода countriesDao, который возвращает объект CountriesDao. Метод будет использоваться для получения DAO объекта для выполнения операций с бд

@Database(
    entities = [
        CountriesEntity::class,
        UniversitiesEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun countriesDao(): CountriesDao

    abstract fun universitiesDao(): UniversitiesDao
}

