package com.imalat.beeSystem.model.yandexAdres


import com.google.gson.annotations.SerializedName

data class YandexAdresCevap(
    @SerializedName("response")
    val response: Response
)