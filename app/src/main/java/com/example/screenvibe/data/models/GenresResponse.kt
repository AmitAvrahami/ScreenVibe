package com.example.screenvibe.data.models

import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres") val genres: List<Genre> = emptyList()
)
