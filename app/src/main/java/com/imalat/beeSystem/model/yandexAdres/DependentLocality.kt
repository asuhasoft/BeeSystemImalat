package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class DependentLocality(
    @SerializedName("DependentLocalityName")
    val dependentLocalityName: String
)