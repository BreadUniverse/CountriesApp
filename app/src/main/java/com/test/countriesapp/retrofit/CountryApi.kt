package com.test.countriesapp.retrofit

import com.test.countriesapp.model.CountriesModel
import retrofit2.http.GET

@JvmSuppressWildcards
interface CountryApi {

    @GET("v3.1/all")
    suspend fun getCountries(): List<CountriesModel>
}