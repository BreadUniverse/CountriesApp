package com.test.countriesapp.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.test.countriesapp.database.CountriesDao
import com.test.countriesapp.model.CountriesEntity

//аннотация, используемая в классе, чтобы указать, что это класс базы данных Room и определить основные параметры бд
// объявление абстрактного метода countriesDao, который возвращает объект CountriesDao. Метод будет использоваться для получения DAO объекта для выполнения операций с бд

@Database(entities = [CountriesEntity::class], version = 2, exportSchema = true,)
abstract class AppRoomDatabase : RoomDatabase() {
    abstract fun countriesDao(): CountriesDao
}

