package com.test.countriesapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
data class CountriesEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val population: Long,
    val flagImage: String,
    var node: String,
    val isCheck: Boolean,
    val capital: String?
    /*@ColumnInfo(name = "capital")
    val capital: String = "",
    val languages: String*/
    //TODO: обновить бд, добавить столицу, языки, университеты
)
