package com.test.countriesapp.model

import java.lang.reflect.Array

data class CountriesModel(
    val name: NameCountry,
    val population: Long,
    val flags: FlagsImage,
    val capital: List<String>?
    /*val capital: String,
    val languages: String*/
)

data class CapitalCountry(
    val capital: List<String?>
)

data class NameCountry(
    val common: String,
    val official: String
)

data class FlagsImage(
    val png: String,
    val svg: String
)
