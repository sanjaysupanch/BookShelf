package com.example.bookshelf.features.authentication.data.remote

import com.example.bookshelf.features.authentication.data.model.CountryData
import retrofit2.http.GET

interface CountryAPI {

    @GET("data/v1/countries")
    suspend fun getCountryData(): CountryData
}