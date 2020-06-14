package com.imalat.beeSystem.model.urunler


import com.google.gson.annotations.SerializedName

data class UrunCevap(
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Int,
    @SerializedName("UrunJSON")
    val urunJSON: List<UrunJSON>
)