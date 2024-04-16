package com.test.countriesapp.model

data class ScreenState(
    val countriesList: List<CountriesEntity>,
    val isErrorVisibility: Boolean
)
