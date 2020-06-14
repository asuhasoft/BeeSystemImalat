package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class MetaDataProperty(
    @SerializedName("GeocoderMetaData")
    val geocoderMetaData: GeocoderMetaData
)