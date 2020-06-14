package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class GeocoderResponseMetaData(
    @SerializedName("found")
    val found: String,
    @SerializedName("Point")
    val point: PointX,
    @SerializedName("request")
    val request: String,
    @SerializedName("results")
    val results: String
)