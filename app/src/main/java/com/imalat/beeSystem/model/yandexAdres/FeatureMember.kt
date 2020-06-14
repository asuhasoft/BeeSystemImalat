package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class FeatureMember(
    @SerializedName("GeoObject")
    val geoObject: GeoObject
)