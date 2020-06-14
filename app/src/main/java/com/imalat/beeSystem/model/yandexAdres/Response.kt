package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class Response(
    @SerializedName("GeoObjectCollection")
    val geoObjectCollection: GeoObjectCollection
)