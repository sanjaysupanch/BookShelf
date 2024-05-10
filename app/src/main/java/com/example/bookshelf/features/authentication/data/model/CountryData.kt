package com.example.bookshelf.features.authentication.data.model

import com.google.gson.annotations.SerializedName

data class CountryData(
    @SerializedName("status")
    val status: String,
    @SerializedName("statusCode")
    val statusCode: Int,
    @SerializedName("data")
    val data: Map<String, Country>
)

data class Country(
    @SerializedName("country")
    val country: String,
    @SerializedName("region")
    val region: String
)