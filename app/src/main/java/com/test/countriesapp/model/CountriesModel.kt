package com.test.countriesapp.model


data class CountriesModel(
    val name: NameCountry,
    val population: Long,
    val flags: FlagsImage,
    val capital: List<String>?,
)

data class NameCountry(
    val common: String,
    val official: String
)

data class FlagsImage(
    val png: String,
    val svg: String
)



