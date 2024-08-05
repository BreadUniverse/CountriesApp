package com.test.countriesapp.retrofit

import com.test.countriesapp.model.NameCountry
import com.test.countriesapp.model.universities.UniversitiesModel
import retrofit2.http.GET
import retrofit2.http.Query

interface UniversityApi {

    @GET("search")
    suspend fun getUniversities(@Query("country") countryName: String): List<UniversitiesModel>

}