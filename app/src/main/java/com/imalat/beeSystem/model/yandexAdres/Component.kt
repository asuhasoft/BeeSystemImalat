package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class Component(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("name")
    val name: String
)