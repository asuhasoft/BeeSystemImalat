package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class MetaDataPropertyX(
    @SerializedName("GeocoderResponseMetaData")
    val geocoderResponseMetaData: GeocoderResponseMetaData
)