package com.imalat.beeSystem.model.genelCevap


import com.google.gson.annotations.SerializedName

data class GenelCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("OturumKodu")
    val oturumKodu: String
)