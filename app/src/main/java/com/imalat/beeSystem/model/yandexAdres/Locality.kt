package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class Locality(
    @SerializedName("DependentLocality")
    val dependentLocality: DependentLocality,
    @SerializedName("Thoroughfare")
    val thoroughfare: Thoroughfare
)