package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class AddressDetails(
    @SerializedName("Country")
    val country: Country
)