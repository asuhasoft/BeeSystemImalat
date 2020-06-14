package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class BoundedBy(
    @SerializedName("Envelope")
    val envelope: Envelope
)