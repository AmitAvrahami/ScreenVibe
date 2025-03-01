package com.example.screenvibe.data.models

import com.google.gson.annotations.SerializedName

data class ProductionCountry(
    @SerializedName("iso_3166_1") val countryCode: String,
    val name: String
)