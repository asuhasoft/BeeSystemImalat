package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class Thoroughfare(
    @SerializedName("ThoroughfareName")
    val thoroughfareName: String
)