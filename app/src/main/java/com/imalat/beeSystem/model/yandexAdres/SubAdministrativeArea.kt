package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class SubAdministrativeArea(
    @SerializedName("Locality")
    val locality: Locality,
    @SerializedName("SubAdministrativeAreaName")
    val subAdministrativeAreaName: String
)